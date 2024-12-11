package org.campus.connect.message.message;

import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.constants.Enums.UserRoles;
import org.campus.connect.message.course.Course;
import org.campus.connect.message.course.CourseDTO;
import org.campus.connect.message.course.CourseMapper;
import org.campus.connect.message.course.CourseService;
import org.campus.connect.message.firebase.FirebaseMessageDTO;
import org.campus.connect.message.firebase.FirebaseService;
import org.campus.connect.message.links.LinksDTO;
import org.campus.connect.message.links.LinksService;
import org.campus.connect.message.message.mobile.InfosDTO;
import org.campus.connect.message.message.view.View;
import org.campus.connect.message.message.view.ViewRepository;
import org.campus.connect.message.users.UsersRepository;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.campus.connect.message.utils.dtos.FilterDTO;
import org.campus.connect.message.utils.dtos.PageableDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends GenericServiceImpl<Message, MessageDTO> implements MessageService {
  private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

  private final MessageRepository repository;
  private final MessageMapper mapper;
  private final LinksService linksService;
  private final FirebaseService firebaseService;
  private final CourseService courseService;
  private final UsersRepository usersRepository;
  private final ViewRepository viewRepository;
  private final CourseMapper courseMapper;

  public MessageServiceImpl(
    final MessageRepository repository,
    final MessageMapper mapper,
    final LinksService linksService, final FirebaseService firebaseService,
    final CourseService courseService, final UsersRepository usersRepository,
    final ViewRepository viewRepository, final CourseMapper courseMapper) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.linksService = linksService;
    this.firebaseService = firebaseService;
    this.courseService = courseService;
    this.usersRepository = usersRepository;
    this.viewRepository = viewRepository;
    this.courseMapper = courseMapper;
  }

  @Override
  public MessageDTO findMsgById(final UUID id) {
    return this.repository.findById(id)
      .map(msg -> {
        MessageDTO dto = mapper.toDto(msg);
        dto.setVlrViews(getViewsQtd(id, msg.getCourses()));
        return dto;
      })
      .orElse(null);
  }

  private String getViewsQtd(UUID id, List<Course> courses) {
    Number views = this.repository.getLength(id);
    final AtomicInteger vlr = new AtomicInteger();
    courses.forEach(course -> {
      vlr.addAndGet(this.repository.getAlunos(course.getId(), UserRoles.USER).intValue());
    });

    return views + "/" + vlr.get();
  }


  @Override
  public List<MessageDTO> findAll() {
    List<Message> messages = repository.findMessages();
    return messages.stream()
      .map(message -> {
        MessageDTO dto = mapper.toDto(message);
        dto.setVlrViews(getViewsQtd(message.getId(), message.getCourses()));
        return dto;
      })
      .collect(Collectors.toList());
  }

  @Override
  public MessageDTO create(final MessageDTO msg) throws Exception {
    if (msg.getLinks() != null && !msg.getLinks().isEmpty()) {
      msg.getLinks().forEach(link -> {
        if (link.getId() == null) {
          try {
            LinksDTO savedLink = this.linksService.create(link);
            link.setId(savedLink.getId());
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      });
    }
    return this.save(msg);
  }

  @Override
  public MessageDTO send(final MessageDTO msg) throws Exception {
    return getMessageDTO(msg);
  }

  @Override
  public MessageDTO sendById(final UUID id) throws Exception {
    MessageDTO msg = findMsgById(id);
    return getMessageDTO(msg);
  }

  private MessageDTO getMessageDTO(final MessageDTO msg) throws Exception {
    if (msg.getCourses().isEmpty()) {
      return null;
    }
    msg.setSendDate(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("-03:00")));
    msg.setStatus(Status.ENVIADO);
    MessageDTO obj = this.create(msg);
    this.firebaseSend(obj);
    obj.setVlrViews(getViewsQtd(msg.getId(), courseMapper.toEntity(msg.getCourses())));
    return obj;
  }

  private List<List<String>> partitionList(List<String> tokens) {
    List<List<String>> partitions = new ArrayList<>();
    for (int i = 0; i < tokens.size(); i += 500) {
      partitions.add(tokens.subList(i, Math.min(i + 500, tokens.size())));
    }
    return partitions;
  }

  private void firebaseSend(final MessageDTO msg) throws ExecutionException, InterruptedException {
    for (CourseDTO c : msg.getCourses()) {
      CourseDTO group = courseService.findById(c.getCourseGroupId());
      String course = c.getName();
      List<FirebaseMessageDTO> tokens = firebaseService.fetchTokens(course, group.getName());
      List<String> notificationTokens = tokens.stream()
        .map(FirebaseMessageDTO::getNotificationToken)
        .toList();

      List<List<String>> tokenBatches = partitionList(notificationTokens);

      for (List<String> batch : tokenBatches) {
        try {
          this.firebaseService.sendMultiNotification(FirebaseMessageDTO.builder()
            .id(msg.getId().toString())
            .title(msg.getTitle())
            .body(msg.getSummary())
            .build(), batch);
        } catch (FirebaseMessagingException e) {
          logger.error("Erro ao enviar lote de notificações: {}", e.getMessage(), e);
        }
      }
    }
  }

  @Override
  public List<MessageDTO> findByIdCourseMobile(UUID id) {
    List<Message> messages = repository.findAllByCourseIdAndStatusEnviado(id);
    return this.mapper.toDto(messages);
  }

  @Override
  public List<MessageDTO> findByIdCourse(UUID id) {
    Pageable top5 = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "sendDate"));
    List<Message> messages = repository.findByCourseIdAndStatusEnviado(id, top5).getContent();
    return messages.stream().map(msg -> {
      MessageDTO dto = this.mapper.toDto(msg);
      dto.setVlrViews(getViewsQtd(msg.getId(), msg.getCourses()));
      return dto;
    }).collect(Collectors.toList());
  }


  @Override
  public List<MessageDTO> findByListIdResp(UUID id) {
    Pageable top5 = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "sendDate"));
    List<Message> messages = repository.findByResponsibleAndStatusEnviado(id, top5).getContent();
    return messages.stream().map(msg -> {
      MessageDTO dto = this.mapper.toDto(msg);
      dto.setVlrViews(getViewsQtd(msg.getId(), msg.getCourses()));
      return dto;
    }).collect(Collectors.toList());
  }

  @Override
  public InfosDTO findInfosAluno(UUID alunoId) {
    return usersRepository.findById(alunoId)
      .map(user -> {
        InfosDTO dto = new InfosDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        List<MessageDTO> allMessages = this.findByIdCourseMobile(user.getId_curso());
        dto.setTotal(allMessages.size());
        List<View> userViews = viewRepository.findAllByUser(alunoId);
        dto.setFavorites(allMessages.stream()
          .filter(message -> userViews.stream()
            .anyMatch(view -> view.getMessage().getId().equals(message.getId()) && view.isFavorite()))
          .count());
        dto.setReads(allMessages.stream()
          .filter(message -> userViews.stream()
            .anyMatch(view -> view.getMessage().getId().equals(message.getId()) && view.isViewed()))
          .count());
        dto.setNotReads(allMessages.size() - dto.getReads().intValue());
        return dto;
      })
      .orElse(null);
  }

  @Override
  public List<MessageDTO> findMessagesByFlagAndStudent(UUID alunoId, String flag) {
    return usersRepository.findById(alunoId)
      .map(user -> {
        List<MessageDTO> allMessages = this.findByIdCourseMobile(user.getId_curso());
        List<View> userViews = viewRepository.findAllByUser(alunoId);
        allMessages.forEach(message -> {
          View view = userViews.stream()
            .filter(v -> v.getMessage().getId().equals(message.getId()))
            .findFirst()
            .orElse(null);
          message.setDate_view(view != null ? view.getViewDate() : null);
          message.setFavorite(view != null && view.isFavorite());
          message.setRead(view != null && view.isViewed());
        });
        return switch (flag.toLowerCase()) {
          case "all" -> allMessages;
          case "reads" -> allMessages.stream()
            .filter(MessageDTO::getRead)
            .collect(Collectors.toList());
          case "not_read" -> allMessages.stream()
            .filter(message -> !message.getRead())
            .collect(Collectors.toList());
          case "favorites" -> allMessages.stream()
            .filter(MessageDTO::getFavorite)
            .collect(Collectors.toList());
          default -> throw new IllegalArgumentException("Flag inválida: " + flag);
        };
      })
      .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado com ID: " + alunoId));
  }

  @Override
  public MessageDTO findMsgByIdMobile(final UUID idMsg, final UUID alunoId) {
    return repository.findById(idMsg)
      .map(msg -> {
        MessageDTO dto = mapper.toDto(msg);
        viewRepository.findByIdUserAndIdMessage(alunoId, idMsg)
          .ifPresent(view -> {
            dto.setRead(view.isViewed());
            dto.setFavorite(view.isFavorite());
            dto.setDate_view(view.getViewDate());
          });
        return dto;
      })
      .orElse(null);
  }

  @Override
  public MessageDTO removeMessage(UUID msgId) throws Exception {
    return repository.findById(msgId)
      .map(msg -> {
        msg.setStatus(Status.REMOVIDA);
        Message savedMsg = repository.save(msg);
        MessageDTO dto = mapper.toDto(savedMsg);
        dto.setVlrViews(getViewsQtd(msgId, savedMsg.getCourses()));
        return dto;
      })
      .orElse(null);
  }

  @Override
  public List<MessageDTO> findAllResp(UUID id) {
    List<Message> messages = repository.findAllByResponsible(id);
    return messages.stream().map(msg -> {
      MessageDTO dto = this.mapper.toDto(msg);
      dto.setVlrViews(getViewsQtd(msg.getId(), msg.getCourses()));
      return dto;
    }).collect(Collectors.toList());
  }

  @Override
  public Page<MessageDTO> searchMessages(PageableDTO pageableDTO) {
    Pageable springPageable;
    Specification<Message> specification = createSpecification(pageableDTO);

    if ("vlrViews".equals(pageableDTO.getSortField())) {
      springPageable = PageRequest.of(pageableDTO.getFirst() / pageableDTO.getRows(), pageableDTO.getRows());
      List<Message> messages = repository.findAll(specification);

      List<MessageDTO> messageDTOs = messages.stream().map(msg -> {
        MessageDTO dto = mapper.toDto(msg);
        dto.setVlrViews(getViewsQtd(msg.getId(), msg.getCourses()));
        return dto;
      }).sorted((m1, m2) -> {
        int comparison = m1.getVlrViews().compareTo(m2.getVlrViews());
        return pageableDTO.getSortOrder() == 1 ? comparison : -comparison;
      }).collect(Collectors.toList());

      int start = Math.min((int) springPageable.getOffset(), messageDTOs.size());
      int end = Math.min((start + springPageable.getPageSize()), messageDTOs.size());
      List<MessageDTO> paginatedList = messageDTOs.subList(start, end);
      return new PageImpl<>(paginatedList, springPageable, messageDTOs.size());
    } else {
      springPageable = createSpringPageable(pageableDTO);
      Page<Message> messages = repository.findAll(specification, springPageable);

      return messages.map(msg -> {
        MessageDTO dto = mapper.toDto(msg);
        dto.setVlrViews(getViewsQtd(msg.getId(), msg.getCourses()));
        return dto;
      });
    }
  }

  private Pageable createSpringPageable(PageableDTO pageableDTO) {
    if (pageableDTO.getSortField() != null) {
      Sort sort = Sort.by(pageableDTO.getSortField());
      sort = pageableDTO.getSortOrder() == 1 ? sort.ascending() : sort.descending();
      return PageRequest.of(pageableDTO.getFirst() / pageableDTO.getRows(), pageableDTO.getRows(), sort);
    }
    return PageRequest.of(pageableDTO.getFirst() / pageableDTO.getRows(), pageableDTO.getRows());
  }

  private Specification<Message> createSpecification(PageableDTO pageableDTO) {
    return (root, query, cb) -> {
      Predicate predicate = cb.conjunction();

      // Se existirem filtros, aplique-os
      if (pageableDTO.getFilters() != null && !pageableDTO.getFilters().isEmpty()) {
        for (Map.Entry<String, FilterDTO> entry : pageableDTO.getFilters().entrySet()) {
          String field = entry.getKey();
          FilterDTO filter = entry.getValue();

          // Verifique se o filtro possui um valor válido
          if (filter.getValue() != null && !filter.getValue().toString().isEmpty()) {
            String value = filter.getValue().toString();

            // Lógica para aplicar filtros com base no campo
            switch (field) {
              case "title":
              case "summary":
              case "message":
                if ("contains".equals(filter.getMatchMode())) {
                  predicate = cb.and(predicate, cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%"));
                }
                break;
              case "custom_status":
                if ("equals".equals(filter.getMatchMode())) {
                  predicate = cb.and(predicate, cb.equal(cb.lower(root.get("status")), value.toLowerCase()));
                }
                break;
              case "sendDate":
                if ("contains".equals(filter.getMatchMode())) {
                  try {
                    LocalDate date = LocalDate.parse(value);
                    predicate = cb.and(predicate, cb.between(root.get("sendDate"), date.atStartOfDay(), date.plusDays(1).atStartOfDay()));
                  } catch (DateTimeParseException e) {
                    // Handle parsing error
                  }
                }
                break;
              case "courses":
                Join<Message, Course> courseJoin = root.join("courses", JoinType.LEFT);
                if ("contains".equals(filter.getMatchMode())) {
                  predicate = cb.and(predicate, cb.like(cb.lower(courseJoin.get("name")), "%" + value.toLowerCase() + "%"));
                }
                break;
            }
          }
        }
      }

      // Verifique o filtro global, se estiver presente
      if (pageableDTO.getGlobalFilter() != null && !pageableDTO.getGlobalFilter().isEmpty()) {
        String globalValue = "%" + pageableDTO.getGlobalFilter().toLowerCase() + "%";
        predicate = cb.and(predicate, cb.or(
          cb.like(cb.lower(root.get("title")), globalValue),
          cb.like(cb.lower(root.get("summary")), globalValue),
          cb.like(cb.lower(root.get("message")), globalValue),
          cb.like(cb.lower(root.get("status").as(String.class)), globalValue)
        ));
        Join<Message, Course> courseJoin = root.join("courses", JoinType.LEFT);
        predicate = cb.and(predicate, cb.or(
          cb.like(cb.lower(courseJoin.get("name")), globalValue),
          cb.like(cb.lower(courseJoin.get("abbreviation")), globalValue)
        ));
      }

      // Caso algum outro filtro global seja necessário, pode ser adicionado aqui
      return predicate;
    };
  }

//  @Override
//  public Page<MessageDTO> searchMessages(PageableDTO pageableDTO) {
//    Pageable springPageable;
//    Specification<Message> specification = createSpecification(pageableDTO);
//
//    if ("vlrViews".equals(pageableDTO.getSortField())) {
//      springPageable = PageRequest.of(pageableDTO.getFirst() / pageableDTO.getRows(), pageableDTO.getRows());
//      List<Message> messages = repository.findAll(specification);
//
//      List<MessageDTO> messageDTOs = messages.stream().map(msg -> {
//        MessageDTO dto = mapper.toDto(msg);
//        dto.setVlrViews(getViewsQtd(msg.getId(), msg.getCourses()));
//        return dto;
//      }).sorted((m1, m2) -> {
//        int comparison = m1.getVlrViews().compareTo(m2.getVlrViews());
//        return pageableDTO.getSortOrder() == 1 ? comparison : -comparison;
//      }).collect(Collectors.toList());
//      int start = Math.min((int) springPageable.getOffset(), messageDTOs.size());
//      int end = Math.min((start + springPageable.getPageSize()), messageDTOs.size());
//      List<MessageDTO> paginatedList = messageDTOs.subList(start, end);
//      return new PageImpl<>(paginatedList, springPageable, messageDTOs.size());
//    } else {
//      springPageable = createSpringPageable(pageableDTO);
//      Page<Message> messages = repository.findAll(specification, springPageable);
//
//      return messages.map(msg -> {
//        MessageDTO dto = mapper.toDto(msg);
//        dto.setVlrViews(getViewsQtd(msg.getId(), msg.getCourses()));
//        return dto;
//      });
//    }
//  }
//
//  private Pageable createSpringPageable(PageableDTO pageableDTO) {
//    if (pageableDTO.getSortField() != null) {
//      Sort sort = Sort.by(pageableDTO.getSortField());
//      sort = pageableDTO.getSortOrder() == 1 ? sort.ascending() : sort.descending();
//      return PageRequest.of(pageableDTO.getFirst() / pageableDTO.getRows(), pageableDTO.getRows(), sort);
//    }
//    return PageRequest.of(pageableDTO.getFirst() / pageableDTO.getRows(), pageableDTO.getRows());
//  }
//
//  private Specification<Message> createSpecification(PageableDTO pageableDTO) {
//    return (root, query, cb) -> {
//      Predicate predicate = cb.conjunction();
//
//      if (pageableDTO.getGlobalFilter() != null && !pageableDTO.getGlobalFilter().isEmpty()) {
//        String filterValue = "%" + pageableDTO.getGlobalFilter().toLowerCase() + "%";
//        String[] dateParts = filterValue.split("[/\\-]");
//        if (dateParts.length == 2) {
//          try {
//            int day = Integer.parseInt(dateParts[0]);
//            int month = Integer.parseInt(dateParts[1]);
//
//            LocalDateTime startDate = LocalDateTime.of(LocalDate.now().getYear(), month, day, 0, 0, 0);
//            LocalDateTime endDate = LocalDateTime.of(LocalDate.now().getYear(), month, day, 23, 59, 59);
//
//            predicate = cb.and(predicate,
//              cb.between(root.get("sendDate"), startDate, endDate));
//          } catch (NumberFormatException ignored) {
//
//          }
//        }
//        predicate = setPredicate(predicate, cb, root, filterValue);
//        query.distinct(true);
//      }
//      if (pageableDTO.getFlag() != null && !pageableDTO.getFlag().isEmpty() && !pageableDTO.getFlag().equals("all")) {
//        predicate = cb.and(predicate, cb.like(root.get("status"), "%" + pageableDTO.getFlag() + "%"));
//      }
//      if (pageableDTO.getObjectId() != null) {
//        predicate = cb.and(predicate, cb.equal(root.get("responsible"), pageableDTO.getObjectId()));
//      }
//      return predicate;
//    };
//  }
//
//  private Predicate setPredicate(Predicate predicate,
//                                 CriteriaBuilder cb,
//                                 Root<Message> root,
//                                 String filterValue) {
//    predicate = cb.and(predicate,
//      cb.or(
//        cb.like(cb.lower(root.get("title")), filterValue),
//        cb.like(cb.lower(root.get("summary")), filterValue),
//        cb.like(cb.lower(root.get("message")), filterValue),
//        cb.like(cb.lower(root.get("status").as(String.class)), filterValue)
//      )
//    );
//    Join<Message, Course> courseJoin = root.join("courses", JoinType.LEFT);
//    predicate = cb.and(predicate,
//      cb.or(
//        cb.like(cb.lower(courseJoin.get("name")), filterValue),
//        cb.like(cb.lower(courseJoin.get("abbreviation")), filterValue)
//      )
//    );
//    return predicate;
//  }

}


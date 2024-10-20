package org.campus.connect.message.course;

import org.campus.connect.message.users.UsersDTO;
import org.campus.connect.message.users.UsersMapper;
import org.campus.connect.message.users.UsersRepository;
import org.campus.connect.message.course.dto.CourseCompleteDTO;
import org.campus.connect.message.course.dto.SubCourseDTO;
import org.campus.connect.message.files.FileMapper;
import org.campus.connect.message.files.FileService;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, CourseDTO> implements CourseService {
  final CourseRepository repository;
  final CourseMapper mapper;
  final UsersMapper usersMapper;
  final UsersRepository usersRepository;
  final FileService fileService;
  final FileMapper fileMapper;

  public CourseServiceImpl(final CourseRepository repository,
                           final CourseMapper mapper, final UsersMapper usersMapper,
                           final UsersRepository usersRepository,
                           final FileService fileService, final FileMapper fileMapper
  ) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.usersMapper = usersMapper;
    this.usersRepository = usersRepository;
    this.fileService = fileService;
    this.fileMapper = fileMapper;
  }

  @Override
  public List<CourseCompleteDTO> findAll(Boolean isGroup) {
    List<CourseDTO> courseDTOs = isGroup ? getList() : repository.findCourses(false);

    return courseDTOs.stream()
      .map(CourseCompleteDTO::new)
      .sorted(Comparator.comparing(CourseCompleteDTO::getId))
      .toList();
  }

//  private void setResp(final CourseCompleteDTO dto, final CourseDTO c) {
//    UsersDTO usr = this.usersRepository.getRespById(c.getResp());
//    if (usr != null) {
//      usr.setProfilePhoto(fileService.findByIdExt(usr.getId()));
//    }
//    dto.setResp(usr);
//  }

  private List<CourseDTO> getList() {
    List<CourseDTO> courseDTOs = repository.findCourses(true);

    courseDTOs.forEach(courseDTO -> {
      List<SubCourseDTO> subCourseDTOs = repository.findSubsByIdGroup(courseDTO.getId());
      subCourseDTOs.forEach(subCourseDTO -> {
        subCourseDTO.setSiglaGroup(courseDTO.getAbbreviation());
        if (subCourseDTO.getResp() == null) {
          subCourseDTO.setResp(courseDTO.getResp());
          CourseDTO saveDto = new CourseDTO(subCourseDTO);
          try {
            this.save(saveDto);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      });
      courseDTO.setCourses(subCourseDTOs);
    });

    courseDTOs.addAll(repository.findCoursesNoGrouped());

    return courseDTOs;
  }

  public CourseDTO findById(final Long idCurso) {
    CourseDTO course = this.repository.findCourseById(idCurso);
    if (course.getIsGroup()) {
      course.setCourses(this.repository.findSubsByIdGroup(idCurso));
    }
    return course;
  }

  @Override
  public List<CourseDTO> findGroups() {
    List<Course> all = this.repository.findAllByIsGroupIsTrue();
    List<CourseDTO> dto = all.stream()
      .map(course -> {
        CourseDTO courseDTO = new CourseDTO(course.getId(), course.getName(),
          course.getAbbreviation(), new UsersDTO(course.getResp()));
        List<SubCourseDTO> subs = repository.findSubsByIdGroup(course.getId()).stream()
          .map(sub -> new SubCourseDTO(sub.getId(), sub.getName(), sub.getAbbreviation()))
          .collect(Collectors.toList());
        courseDTO.setCourses(subs);

        return courseDTO;
      })
      .collect(Collectors.toList());
    List<CourseDTO> subs = this.repository.findCoursesNoGrouped();
    CourseDTO courseDTO = new CourseDTO();
    courseDTO.setName("Outros");
    List<SubCourseDTO> noGroupedCourses = subs.stream()
      .map(c -> new SubCourseDTO(c.getId(), c.getName(), c.getAbbreviation())).toList();
    courseDTO.setCourses(noGroupedCourses);
    dto.add(courseDTO);
    return dto;
  }

  @Override
  public CourseDTO create(CourseDTO dto) throws Exception {
    if (dto.getIsGroup()) {
      if (dto.getCourseGroupId() != null) {
        throw new IllegalArgumentException("Um grupo não pode ser atribuído a outro grupo.");
      }
      dto.setCourseGroupId(null);
    } else {
      if (dto.getCourseGroupId() != null) {
        Optional<Course> group = repository.findById(dto.getCourseGroupId());
        if (group.isPresent()) {
          if (!group.get().getIsGroup()) {
            throw new IllegalArgumentException("O ID fornecido não é de um grupo válido.");
          }
        } else {
          throw new IllegalArgumentException("Grupo não encontrado para o ID fornecido.");
        }
      }
    }
    return this.save(dto);
  }

  @Override
  public CourseDTO update(final CourseDTO dto) {
    return null;
  }

  @Override
  public CourseCompleteDTO findCourseById(Long id) {
    CourseDTO course = findById(id);
    return new CourseCompleteDTO(course);
  }

  @Override
  public List<CourseDTO> findAll() {
    List<Course> courses = this.repository.findAll();
    return this.mapper.toDto(courses);
  }

}

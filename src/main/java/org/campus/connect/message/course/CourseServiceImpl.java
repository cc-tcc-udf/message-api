package org.campus.connect.message.course;

import org.campus.connect.message.course.dto.CourseCompleteDTO;
import org.campus.connect.message.course.dto.SubCourseDTO;
import org.campus.connect.message.files.FileMapper;
import org.campus.connect.message.files.FileService;
import org.campus.connect.message.firebase.FirebaseService;
import org.campus.connect.message.users.UsersDTO;
import org.campus.connect.message.users.UsersMapper;
import org.campus.connect.message.users.UsersRepository;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, CourseDTO> implements CourseService {
  final CourseRepository repository;
  final CourseMapper mapper;
  final UsersMapper usersMapper;
  final UsersRepository usersRepository;
  final FileService fileService;
  final FileMapper fileMapper;
  private final FirebaseService firebaseService;

  public CourseServiceImpl(
    final CourseRepository repository,
    final CourseMapper mapper, final UsersMapper usersMapper,
    final UsersRepository usersRepository,
    final FileService fileService, final FileMapper fileMapper,
    final FirebaseService firebaseService
  ) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.usersMapper = usersMapper;
    this.usersRepository = usersRepository;
    this.fileService = fileService;
    this.fileMapper = fileMapper;
    this.firebaseService = firebaseService;
  }

  @Override
  public List<CourseCompleteDTO> findAll(Boolean isGroup) {
    List<CourseDTO> courseDTOs = isGroup ? getList() : repository.findCourses(false);

    return courseDTOs.stream()
      .map(CourseCompleteDTO::new)
      .sorted(Comparator.comparing(CourseCompleteDTO::getId))
      .toList();
  }

  @Override
  public List<CourseDTO> getCoursesByResp(UUID id) {
    return repository.findCoursesResp(id);
  }

  private List<CourseDTO> getList() {
    List<CourseDTO> courseDTOs = repository.findCourses(true);
    courseDTOs.forEach(courseDTO -> {
      List<SubCourseDTO> subCourseDTOs = repository.findSubsByIdGroup(courseDTO.getId());
      subCourseDTOs.forEach(subCourseDTO -> {
        subCourseDTO.setSiglaGroup(courseDTO.getAbbreviation());
        if (subCourseDTO.getResp() != courseDTO.getResp()) {
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

  @Override
  public CourseDTO findById(final UUID idCurso) {
    CourseDTO course = this.repository.findCourseById(idCurso);
    if (course.getIsGroup()) {
      course.setCourses(this.repository.findSubsByIdGroup(idCurso));
    }
    return course;
  }

  @Override
  public CourseDTO getMinimalById(final UUID idCurso) {
    CourseDTO course = this.findById(idCurso);
    return new CourseDTO(course.getId(), course.getName(), course.getAbbreviation(), course.getResp());
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
    if (!subs.isEmpty()) {
      CourseDTO courseDTO = new CourseDTO();
      courseDTO.setName("Outros");
      List<SubCourseDTO> noGroupedCourses = subs.stream()
        .map(c -> new SubCourseDTO(c.getId(), c.getName(), c.getAbbreviation())).toList();
      courseDTO.setCourses(noGroupedCourses);
      dto.add(courseDTO);
    }
    return dto;
  }

  @Override
  public List<CourseDTO> findGroupsMobile() {
    List<Course> all = this.repository.findAllByIsGroupIsTrue();
    Map<UUID, List<SubCourseDTO>> groupedSubCourses = this.repository
      .findAllSubs().stream().collect(
        Collectors.groupingBy(
          SubCourseDTO::getCourseGroupId,
          Collectors.toList()
        )
      );
    List<CourseDTO> dto = all.stream()
      .map(course -> CourseDTO.builder()
        .id(course.getId())
        .name(course.getName())
        .abbreviation(course.getAbbreviation())
        .resp(new UsersDTO(course.getResp()))
        .courses(groupedSubCourses.get(course.getId()))
        .build()
      ).collect(Collectors.toList());


    List<CourseDTO> noGrouped = this.repository.findCoursesNoGrouped();
    if (!noGrouped.isEmpty()) {
      List<SubCourseDTO> noGroupedSubCourses = noGrouped.stream()
        .map(course -> SubCourseDTO.builder()
          .id(course.getId())
          .name(course.getName())
          .abbreviation(course.getAbbreviation())
          .build()
        ).collect(Collectors.toList());
      dto.add(CourseDTO.builder()
        .name("Outros")
        .courses(noGroupedSubCourses)
        .build());
    }
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
    CourseDTO newDto = this.save(dto);
    if (!newDto.getIsGroup()) {
      if(newDto.getCourseGroupId() != null) {
      CourseDTO c = findById(newDto.getCourseGroupId());
      firebaseService.createCollection(newDto, c.getName());
      }else{
      firebaseService.createCollection(newDto);
      }
    }
    return newDto;
  }

  @Override
  public CourseDTO update(final CourseDTO dto) {
    return null;
  }

  @Override
  public CourseCompleteDTO findCourseById(UUID id) {
    CourseDTO course = findById(id);
    return new CourseCompleteDTO(course);
  }

  @Override
  public List<CourseDTO> findAll() {
    List<Course> courses = this.repository.findAll();
    return this.mapper.toDto(courses);
  }

}

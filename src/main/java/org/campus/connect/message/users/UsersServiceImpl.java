package org.campus.connect.message.users;

import org.campus.connect.message.constants.Enums.UserRoles;
import org.campus.connect.message.course.CourseDTO;
import org.campus.connect.message.course.CourseServiceImpl;
import org.campus.connect.message.files.FileDTO;
import org.campus.connect.message.files.FileMapper;
import org.campus.connect.message.files.FileService;
import org.campus.connect.message.infra.exceptions.ResourceNotFoundException;
import org.campus.connect.message.mail.MailDTO;
import org.campus.connect.message.mail.MailService;
import org.campus.connect.message.users.records.RegisterDTO;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl extends GenericServiceImpl<Users, UsersDTO> implements UsersService {

  private final UsersRepository repository;
  private final UsersMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final FileService fileService;
  private final FileMapper fileMapper;
  private final CourseServiceImpl courseService;
  private final MailService mailService;
  private final String frontUrl;

  public UsersServiceImpl(
    final UsersRepository repository,
    final UsersMapper mapper, final PasswordEncoder passwordEncoder,
    final FileService fileService, final FileMapper fileMapper,
    final CourseServiceImpl courseService,
    @Value("${front.url}") String frontUrl, final MailService mailService) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.passwordEncoder = passwordEncoder;
    this.fileService = fileService;
    this.fileMapper = fileMapper;
    this.courseService = courseService;
    this.mailService = mailService;
    this.frontUrl = frontUrl;
  }

  @Override
  public Optional<Users> findByEmail(String email) {
    return this.repository.findByEmail(email);
  }

  @Override
  public UsersDTO getUserById(UUID id) {
    Optional<Users> user = this.repository.findById(id);
    return user.map(mapper::toDto).orElse(null);
  }

  @Override
  public List<UsersDTO> findAll() {
    List<Users> listUsers = this.repository.findAllByExcluded(Boolean.FALSE);
    listUsers.forEach(user -> {
      user.setPassword(null);
    });
    return this.mapper.toDto(listUsers);
  }

  @Override
  public Users register(RegisterDTO dto, boolean isMobile) throws Exception {
    Users user = new Users(dto);
    user.setRoles(new HashSet<>(dto.getRoles()));
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    this.save(mapper.toDto(user));
    return user;
  }

  @Override
  public Users register_mobile(RegisterDTO dto) throws Exception {
    Users user = new Users(dto);
    user.setRoles(Collections.singleton(UserRoles.USER));
    user.setActive(true);
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    this.save(mapper.toDto(user));
    return user;
  }

  @Override
  public UsersDTO adminCreate() throws Exception {
    Users user = new Users();
    user.setEmail("tauisilva@gmail.com");
    user.setName("Taui Silva Lima");
    user.setPhone("admin");
    user.setActive(true);
    user.setRoles(Collections.singleton(UserRoles.ADMIN));
    user.setPassword(passwordEncoder.encode("sousen1902*"));
    return this.save(mapper.toDto(user));
  }

  @Override
  public UsersDTO createUser(UsersDTO usr) throws Exception {
    if (usr.getId() != null) {
      if (usr.getPassword() != null) {
        usr.setPassword(passwordEncoder.encode(usr.getPassword()));
      }
      return this.save(usr);
    }
    if (usr.isActive() && !usr.isSendMail() && usr.getRoles().contains(UserRoles.PROF)) {
      MailDTO mailDTO = new MailDTO();
      mailDTO.setLink(frontUrl);
      mailDTO.setName(usr.getName());
      mailDTO.setEmail(usr.getEmail());
      mailDTO.setTo(usr.getEmail());
      mailDTO.setPass(usr.getPassword());
      mailService.sendWelcomeEmail(mailDTO);
      usr.setSendMail(true);
    }
    usr.setPassword(passwordEncoder.encode(usr.getPassword()));
    return this.save(usr);
  }

  private UsersDTO updateUser(final UsersDTO usr) {
    return new UsersDTO();
  }

  @Override
  public UsersDTO getUser(Users user) {
    UsersDTO dto = new UsersDTO(user);
    if (user.getId_curso() != null) {
      dto.setCourse(courseService.findById(user.getId_curso()));
    }
    dto.setProfilePhoto(
      user.getProfilePhoto() != null ?
        fileMapper.toDto(user.getProfilePhoto()) :
        fileService.findByIdExt(user.getId())
    );
    return dto;
  }

  @Override
  public UsersDTO getUserMobile(Users user) {
    UsersDTO dto = new UsersDTO(user);
    if (user.getId_curso() != null) {
      CourseDTO course = courseService.findById(user.getId_curso());
      CourseDTO group = courseService.findById(course.getCourseGroupId());
      dto.setGroup(group.getName());
      dto.setCourse(course);
    }
    dto.setProfilePhoto(
      user.getProfilePhoto() != null ?
        fileMapper.toDto(user.getProfilePhoto()) :
        fileService.findByIdExt(user.getId())
    );
    return dto;
  }

  @Override
  public UsersDTO update(final UsersDTO dto) throws Exception {
    Optional<Users> userOptional = findByEmail(dto.getEmail());
    if (userOptional.isPresent()) {
      Users user = userOptional.get();
      dto.setId(user.getId());
      dto.setEmail(user.getEmail());
      dto.setName(dto.getName() != null ? dto.getName() : user.getName());
      dto.setPassword(user.getPassword());
      dto.setActive(user.isActive());
      dto.setPhone(dto.getPhone() != null && !dto.getPhone().isEmpty() ? dto.getPhone() : user.getPhone());
      dto.setId_curso(dto.getId_curso() != null ? dto.getId_curso() : user.getId_curso());
      dto.setRoles(new ArrayList<>(user.getRoles()));
      return save(dto);
    } else {
      throw new ResourceNotFoundException("Usuário não encontrado: " + dto.getEmail());
    }
  }

  @Override
  public List<UsersDTO> findResp() {
    List<Users> users = this.repository.findByRolesContainsAndActive(UserRoles.PROF, true);
    List<CourseDTO> obj = courseService.findAll();
    return users.stream()
      .map(usr -> {
        boolean isResp = obj.stream()
          .anyMatch(course -> course.getResp() != null && course.getResp().getId().equals(usr.getId()));

        if (!isResp) {
          UsersDTO userDTO = new UsersDTO();
          userDTO.setId(usr.getId());
          userDTO.setName(usr.getName());
          userDTO.setId_curso(usr.getId_curso());
          if (usr.getProfilePhoto() != null) {
            userDTO.setProfilePhoto(new FileDTO(usr.getProfilePhoto()));
          }
          return userDTO;
        } else {
          return null;
        }
      })
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }

  @Override
  public List<UsersDTO> findByCourse(UUID id) {
    return this.repository.findByRolesAndIdCursoAndExcludedFalse(UserRoles.USER, id)
      .stream()
      .map(UsersDTO::new)
      .collect(Collectors.toList());
  }
}

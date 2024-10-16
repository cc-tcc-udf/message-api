package org.campus.connect.message.auth.users;

import org.campus.connect.message.auth.users.records.RegisterDTO;
import org.campus.connect.message.constants.Enums.UserRoles;
import org.campus.connect.message.course.CourseServiceImpl;
import org.campus.connect.message.files.FileDTO;
import org.campus.connect.message.files.FileMapper;
import org.campus.connect.message.files.FileService;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersServiceImpl extends GenericServiceImpl<Users, UsersDTO> implements UsersService {

  private final UsersRepository repository;
  private final UsersMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final FileService fileService;
  private final FileMapper fileMapper;
  private final CourseServiceImpl courseService;

  public UsersServiceImpl(
    final UsersRepository repository,
    final UsersMapper mapper, final PasswordEncoder passwordEncoder,
    final FileService fileService, final FileMapper fileMapper,
    final CourseServiceImpl courseService) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.passwordEncoder = passwordEncoder;
    this.fileService = fileService;
    this.fileMapper = fileMapper;
    this.courseService = courseService;
  }

  @Override
  public Optional<Users> findByEmail(String email) {
    return this.repository.findByEmail(email);
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
  public Users register(RegisterDTO dto) throws Exception {
    Users user = new Users();
    user.setEmail(dto.getEmail());
    user.setName(dto.getName());
    user.setUid(UUID.randomUUID());
    user.setRoles(Collections.singleton(UserRoles.USER));
    user.setPhone(dto.getPhone());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    if (dto.getTokens() != null) {
      user.setToken(new HashSet<>(dto.getTokens()));
    }
    this.save(mapper.toDto(user));
    return user;
  }

  @Override
  public UsersDTO adminCreate() throws Exception {
    Users user = new Users();
    user.setEmail("admin@admin.com");
    user.setName("Taui Silva Lima");
    user.setUid(UUID.randomUUID());
    user.setPhone("admin");
    user.setRoles(Collections.singleton(UserRoles.ADMIN));
    user.setPassword(passwordEncoder.encode("sousen1902*"));
    return this.save(mapper.toDto(user));
  }

  @Override
  public UsersDTO createUser(UsersDTO usr) throws Exception {
    usr.setUid(UUID.randomUUID());
    usr.setPassword(passwordEncoder.encode(usr.getPassword()));
    return this.save(usr);
  }

  @Override
  public UsersDTO getUser(Users user) {
    UsersDTO dto = new UsersDTO();
    dto.setEmail(user.getEmail());
    dto.setId(user.getId());
    dto.setName(user.getName());
    dto.setRoles(new ArrayList<>(user.getRoles()));
    dto.setUid(user.getUid());
    dto.setPhone(user.getPhone());
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
  public UsersDTO update(final UsersDTO dto) throws Exception {
    Optional<Users> userOptional = findByEmail(dto.getEmail());
    if (userOptional.isPresent()) {
      Users user = userOptional.get();
      dto.setId(user.getId());
      dto.setEmail(user.getEmail());
      dto.setName(dto.getName() != null ? dto.getName() : user.getName());
      dto.setPassword(user.getPassword());
      dto.setPhone(dto.getPhone() != null && !dto.getPhone().isEmpty() ? dto.getPhone() : user.getPhone());
      dto.setId_curso(dto.getId_curso() != null ? dto.getId_curso() : user.getId_curso());
      dto.setRoles(new ArrayList<>(user.getRoles()));
      dto.setUpdatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : String.valueOf(user.getUid()));
      dto.setUid(dto.getUid() != null ? dto.getUid() : user.getUid() != null ? user.getUid() : UUID.randomUUID());
      return this.save(dto);
    } else {
      throw new Exception("Usuário nao encontrado" + dto.getEmail());
    }
  }

  @Override
  public List<UsersDTO> findResp() {
    List<Users> users = this.repository.findByRolesContains(UserRoles.PROF);
    return users.stream()
      .map(usr -> {
        UsersDTO userDTO = new UsersDTO();
        userDTO.setId(usr.getId());
        userDTO.setName(usr.getName());
        userDTO.setId_curso(usr.getId_curso());
        if(usr.getProfilePhoto()!=null) {
        userDTO.setProfilePhoto(new FileDTO(usr.getProfilePhoto()));
        }
        return userDTO;
      }).toList();
  }

}

package org.campus.connect.message.auth.users;

import org.campus.connect.message.auth.users.records.RegisterDTO;
import org.campus.connect.message.constants.Enums.UserRoles;
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

  public UsersServiceImpl(
    UsersRepository repository,
    UsersMapper mapper, PasswordEncoder passwordEncoder,
    FileService fileService, FileMapper fileMapper) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.passwordEncoder = passwordEncoder;
    this.fileService = fileService;
    this.fileMapper = fileMapper;
  }

  @Override
  public Optional<Users> findByEmail(String email) {
    return this.repository.findByEmail(email);
  }

  @Override
  public List<UsersDTO> findAll() {
    List<Users> listUsers = this.repository.findAllByExcluded(Boolean.FALSE);
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
    user.setCoverPhoto(
      this.fileMapper.toEntity(
        fileService.save(new FileDTO())
      )
    );
    user.setProfilePhoto(
      this.fileMapper.toEntity(
        fileService.save(new FileDTO())
      )
    );
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
  public UsersDTO getUser(Users user) {
    UsersDTO dto = new UsersDTO();
    dto.setEmail(user.getEmail());
    dto.setId(user.getId());
    dto.setName(user.getName());
    dto.setRoles(new ArrayList<>(user.getRoles()));
    dto.setUid(user.getUid());
    dto.setPhone(user.getPhone());
    dto.setCoverPhoto(fileMapper.toDto(user.getCoverPhoto()));
    dto.setProfilePhoto(fileMapper.toDto(user.getProfilePhoto()));
    return dto;
  }

  @Override
  public UsersDTO update(final UsersDTO dto) throws Exception {
    Optional<Users> userOptional = findByEmail(dto.getEmail());
    if (userOptional.isPresent()) {
      Users user = userOptional.get();
      dto.setPassword(user.getPassword());
      dto.setUpdatedBy(String.valueOf(user.getUid()));
    }
    return this.save(dto);
  }

}

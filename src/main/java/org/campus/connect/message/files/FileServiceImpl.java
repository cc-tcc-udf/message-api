package org.campus.connect.message.files;


import org.campus.connect.message.files.FileCloud.CloudinaryService;
import org.campus.connect.message.files.FileCloud.FileDriveService;
import org.campus.connect.message.utils.GenericServiceImpl;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileServiceImpl extends GenericServiceImpl<File, FileDTO> implements FileService {

  private final FileRepository repository;
  private final FileMapper mapper;
  private final Path fileStorageLocation;
  private final Integer maxWidth;
  private final Integer maxHeight;
  private final String profile;
  private final FileDriveService driveService;
  private final CloudinaryService cloudinaryService;

  public FileServiceImpl(
    final FileRepository repository,
    final FileMapper mapper,
    @Value("${server.storagePath}") String storagePath,
    @Value("${image.maxWidth}") Integer maxWidth,
    @Value("${image.maxHeight}") Integer maxHeight,
    @Value("${spring.profiles.active}") String profile,
    final FileDriveService driveService,
    final CloudinaryService cloudinaryService
  ) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
    this.fileStorageLocation = Paths.get(storagePath).toAbsolutePath().normalize();
    this.profile = profile;
    this.driveService = driveService;
    this.cloudinaryService = cloudinaryService;
    if ("local".equals(profile)) {
      try {
        Files.createDirectories(this.fileStorageLocation);
      } catch (Exception ex) {
        throw new RuntimeException("Erro ao criar local de arquivos");
      }
    }
  }

  //Get and Find's
  @Override
  public List<FileDTO> findAll() {
    return this.repository.findAllByExcluded(Boolean.FALSE);
  }

  @Override
  public List<FileDTO> findByIdExt(final Long id) {
    return this.mapper.toDto(this.repository.findAllById_ext(id));
  }

  @Override
  public Resource getFile(Long id) throws Exception {
    Optional<FileDTO> arquivo = findOneById(id);
    if (arquivo.isPresent()) {
      File file = mapper.toEntity(arquivo.get());
      return this.loadFileAsResource(file.getKey());
    }
    return null;
  }

  public Resource loadFileAsResource(String fileName) throws Exception {
    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new FileNotFoundException("File not found " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new FileNotFoundException("File not found " + fileName);
    }
  }

  // Create arquivo
  @Override
  public FileDTO create(final MultipartFile multipartFile, final Long id) throws Exception {
    System.out.println("Profile AQUI: " + profile);

    if ("dev".equals(profile)) {
      FileDTO file = cloudinaryService.uploadToCloudinary(multipartFile);
      file.setId_ext(id);
      file = this.save(file);
      return file;
    }

    return this.createLocal(multipartFile, id);
  }

  private FileDTO createLocal(final MultipartFile multipartFile, final Long id) throws Exception {
    File file = setArquivo(multipartFile, new File(multipartFile));
    file.setId_ext(id);
    this.setStorage(file.getKey(), multipartFile);
    return this.save(mapper.toDto(file));
  }


  //Update arquivo
  @Override
  public FileDTO update(final Long id, final MultipartFile multipartFile) throws Exception {
    Optional<FileDTO> file = findOneById(id);
    if (file.isPresent()) {
      File newFile = setArquivo(multipartFile, new File(multipartFile));
      this.setStorage(newFile.getKey(), multipartFile);
      return save(mapper.toDto(newFile));
    }
    return null;
  }

  public File setArquivo(final MultipartFile multipartFile, final File file) {
    file.setKey(file.getUid() +
      Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(
        multipartFile.getOriginalFilename().lastIndexOf(".")
      ));
    return file;
  }

  private void setStorage(final String key, final MultipartFile file) {
    try {
      Path targetLocation = this.fileStorageLocation.resolve(key);
      InputStream is = new ByteArrayInputStream(file.getBytes());
      BufferedImage bufferedImage = ImageIO.read(is);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      if (bufferedImage.getWidth() > this.maxWidth ||
        bufferedImage.getHeight() > this.maxHeight) {
        BufferedImage imageResized = Scalr.resize(bufferedImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, this.maxWidth, this.maxHeight, Scalr.OP_ANTIALIAS);
        String extension = String.valueOf(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
        ImageIO.write(imageResized, extension, bos);
        is = new ByteArrayInputStream(bos.toByteArray());
        Files.copy(is, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        imageResized.flush();
      } else {
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      }

    } catch (IOException ex) {
      throw new RuntimeException("Não foi possível salvar a foto " + key + ".", ex);
    }
  }

}

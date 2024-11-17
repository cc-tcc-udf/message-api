package org.campus.connect.message.files.FileCloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.campus.connect.message.files.FileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

  private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

  private static final String RESOURCE_TYPE = "resource_type";
  private static final String RAW = "raw";
  private static final String AUTO = "auto";
  private static final String USE_FILENAME = "use_filename";
  private static final String UNIQUE_FILENAME = "unique_filename";
  private static final String OVERWRITE = "overwrite";
  private static final String FOLDER = "folder";

  private final Cloudinary cloudinary;
  private final String folderIMG;
  private final String folderFiles;

  public CloudinaryService(
    @Value("${folder.img}") final String folderIMG,
    @Value("${folder.files}") final String folderFiles,
    @Value("${cloudinary.url}") final String urlCloudinary
  ) {
    this.cloudinary = new Cloudinary(urlCloudinary);
    this.folderIMG = folderIMG;
    this.folderFiles = folderFiles;
    this.cloudinary.config.secure = true; // Força URLs seguras
  }

  /**
   * Faz o upload de um arquivo para o Cloudinary.
   *
   * @param multipartFile arquivo a ser enviado.
   * @return informações do arquivo após o upload.
   * @throws IOException em caso de erro ao processar o arquivo.
   */
  public FileDTO uploadToCloudinary(final MultipartFile multipartFile) throws IOException {
    validateFile(multipartFile);

    FileDTO fileDTO = new FileDTO(multipartFile);
    String folderPath = getFolderPath(multipartFile);
    String resourceType = getResourceType(multipartFile);

    File tempFile = createTemporaryFile(multipartFile);

    try {
      logger.info("Iniciando upload para a pasta: {}", folderPath);

      Map<String, Object> uploadParams = ObjectUtils.asMap(
        RESOURCE_TYPE, resourceType,
        USE_FILENAME, true,
        UNIQUE_FILENAME, false,
        OVERWRITE, true,
        FOLDER, folderPath
      );

      CloudinaryUploadDTO uploadResult = new CloudinaryUploadDTO(cloudinary.uploader().upload(tempFile, uploadParams));
      fileDTO.setUrl(uploadResult.getSecureUrl());

      logger.info("Upload concluído com sucesso: {}", uploadResult.getSecureUrl());
      return fileDTO;

    } finally {
      if (tempFile.delete()) {
        logger.debug("Arquivo temporário deletado: {}", tempFile.getPath());
      } else {
        logger.warn("Não foi possível deletar o arquivo temporário: {}", tempFile.getPath());
      }
    }
  }

  private void validateFile(final MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      throw new IllegalArgumentException("O arquivo fornecido está vazio ou é inválido.");
    }
  }

  private String getFolderPath(final MultipartFile multipartFile) {
    if (multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("image/")) {
      return folderIMG;
    }
    return folderFiles;
  }

  private String getResourceType(final MultipartFile multipartFile) {
    String contentType = multipartFile.getContentType();
    if (contentType != null) {
      if (contentType.startsWith("image/") || contentType.equals("application/pdf")) {
        return AUTO;
      } else {
        return RAW;
      }
    }
    return RAW;
  }

  private File createTemporaryFile(final MultipartFile multipartFile) throws IOException {
    String originalFilename = multipartFile.getOriginalFilename();
    String extension = "";

    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    File tempFile = File.createTempFile(UUID.randomUUID().toString(), extension);

    try (var inputStream = multipartFile.getInputStream()) {
      Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    return tempFile;
  }
}

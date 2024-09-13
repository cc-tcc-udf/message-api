package org.campus.connect.message.files.FileCloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.campus.connect.message.files.FileDTO;
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

  private final Cloudinary cloudinary;
  private final String folderIMG;
  private final String folderFiles;

  public CloudinaryService(
    @Value("${folder.img}") final String folderIMG,
    @Value("${folder.files}") final String folderFiles,
    @Value("${cloudinary.url}") final String urlCloudinary) {
    this.cloudinary = new Cloudinary(urlCloudinary);
    this.folderIMG = folderIMG;
    this.folderFiles = folderFiles;
    this.cloudinary.config.secure = true; // Força URLs seguras
  }

  public FileDTO uploadToCloudinary(final MultipartFile multipartFile) throws Exception {
    // Inicializa o DTO com os dados do arquivo recebido
    FileDTO fileDTO = new FileDTO(multipartFile);
    String folderPath;

    // Define o caminho da pasta com base no tipo de arquivo
    if (multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("image/")) {
      folderPath = this.folderIMG;
    } else {
      folderPath = this.folderFiles;
    }

    // Cria um arquivo temporário a partir do MultipartFile
    File tempFile = getFile(multipartFile);

    // Parâmetros de upload para a Cloudinary
    Map<String, Object> uploadParams = ObjectUtils.asMap(
      "use_filename", true,
      "unique_filename", false,
      "overwrite", true,
      "folder", folderPath
    );

    // Faz o upload para a Cloudinary e obtém o resultado
    CloudinaryUploadDTO uploadResult = new CloudinaryUploadDTO(cloudinary.uploader().upload(tempFile, uploadParams));

    // Apaga o arquivo temporário
    tempFile.delete();

    // Atualiza o fileDTO com os dados do Cloudinary, se necessário
    fileDTO.setUrl(uploadResult.getSecureUrl());

    return fileDTO;
  }

  private File getFile(final MultipartFile multipartFile) throws IOException {
    String originalFilename = multipartFile.getOriginalFilename();
    String extension = "";

    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    // Criação de um arquivo temporário
    File tempFile = File.createTempFile(UUID.randomUUID().toString(), extension);

    try (var inputStream = multipartFile.getInputStream()) {
      Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    return tempFile;
  }

}


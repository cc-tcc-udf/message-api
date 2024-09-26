package org.campus.connect.message.files.FileCloud;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.campus.connect.message.files.FileDTO;
import org.campus.connect.message.files.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

@Service
public class FileDriveService {

  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private final String folderIMG;
  private final String folderFiles;
  private final String urlDrive;
  private final Resource serviceAccountKeyResource;

  public FileDriveService(
    @Value("${folder.img}") final String folderIMG,
    @Value("${folder.files}") final String folderFiles,
    @Value("${drive.url}") final String urlDrive,
    @Value("classpath:secrets/g-drive.json") final Resource serviceAccountKeyResource,
    final FileMapper fileMapper) {
    this.folderIMG = folderIMG;
    this.folderFiles = folderFiles;
    this.urlDrive = urlDrive;
    this.serviceAccountKeyResource = serviceAccountKeyResource;
  }

  public FileDTO createDrive(final MultipartFile multipartFile) throws Exception {
    FileDTO fileDTO = new FileDTO(multipartFile);
    String folderId;

    if (multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("image/")) {
      folderId = this.folderIMG;
    } else {
      folderId = this.folderFiles;
    }

    Drive drive = this.getDriveService();
    File fileMetadata = new File();
    fileMetadata.setName(multipartFile.getOriginalFilename());
    fileMetadata.setOriginalFilename(multipartFile.getOriginalFilename());
    fileMetadata.setParents(Collections.singletonList(folderId));

    java.io.File ioFile = this.getFile(multipartFile);
    FileContent mediaContent = new FileContent(multipartFile.getContentType(), ioFile);
    File file = drive.files().create(fileMetadata, mediaContent).execute();
    fileDTO.setUrl(urlDrive + file.getId());
    fileDTO.setKey(null);
    ioFile.delete(); // Exclui o arquivo temporário

    return fileDTO;
  }

  private java.io.File getFile(final MultipartFile multipartFile) throws IOException {
    String originalFilename = multipartFile.getOriginalFilename();
    String extension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
    }
    java.io.File tempFile = java.io.File.createTempFile(UUID.randomUUID().toString(), extension);
    try (InputStream inputStream = multipartFile.getInputStream()) {
      Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    return tempFile;
  }

  private Drive getDriveService() throws IOException, GeneralSecurityException {
    try (InputStream inputStream = serviceAccountKeyResource.getInputStream()) {
      GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
        .createScoped(Collections.singleton(DriveScopes.DRIVE));
      return new Drive.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        JSON_FACTORY,
        new HttpCredentialsAdapter(credentials)
      ).build();
    }
  }
}

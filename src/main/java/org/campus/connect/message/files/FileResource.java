package org.campus.connect.message.files;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.campus.connect.message.utils.GenericResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api")
@Tag(name = "Files", description = "Gerenciamento de Arquivos")
public class FileResource extends GenericResource<FileDTO, FileResource> {

  private final FileService service;

  public FileResource(final FileService service) {
    super(service, "api/");
    this.service = service;
  }

  @PostMapping(value = "/public/file/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<FileDTO> create(@RequestParam("file") MultipartFile multipartFile) throws Exception {
    FileDTO file = this.service.create(multipartFile);
    return ResponseEntity.ok().body(file);
  }

  @PostMapping(value = "/private/file/{id}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> update(@PathVariable Long id, @RequestParam("file") MultipartFile multipartFile) throws Exception {
    FileDTO file = this.service.update(id, multipartFile);
    if (file != null) {
      return ResponseEntity.ok().body(file);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Arquivo não encontrado\"}");
  }

  @GetMapping(value = "/public/file/get/{id}")
  public ResponseEntity<Resource> download(@PathVariable Long id, HttpServletRequest request) throws Exception {
    Resource resource = this.service.getFile(id);
    String contenType = request.getServletContext()
      .getMimeType(resource.getFile().getAbsolutePath());
    return ResponseEntity.ok()
      .contentType(MediaType.parseMediaType(contenType))
      .header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + resource.getFilename() + "\"")
      .body(resource);
  }

}

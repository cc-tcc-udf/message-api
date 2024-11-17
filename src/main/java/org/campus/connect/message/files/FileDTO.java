package org.campus.connect.message.files;


import lombok.*;
import org.campus.connect.message.utils.AbstractEntityDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FileDTO extends AbstractEntityDTO {
  private String name;
  private String type;
  private String url;
  private Long size;
  private String key;
  private UUID id_ext;

  public FileDTO(File file) {
    this.name = file.getName();
    this.type = file.getType();
    this.url = file.getUrl();
    this.size = file.getSize();
    this.key = file.getKey();
    this.id_ext = file.getId_ext();

  }

  public FileDTO(MultipartFile file) {
    setName(file.getOriginalFilename());
    setSize(file.getSize());
    setType(file.getContentType());
  }

  public String getUrl() {
    if (getKey() != null && getUrl() == null) {
      return UrlConstant.getAbsoluteUrl() + "/api/public/file/get/" + getId();
    }
    return this.url != null ? this.url : null;
  }

  public String getNameNoExtension(String name) {
    int lastDotIndex = name.lastIndexOf('.');
    if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
      return name.substring(0, lastDotIndex);
    }
    return name;
  }
}

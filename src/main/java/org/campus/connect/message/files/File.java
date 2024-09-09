package org.campus.connect.message.files;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.SchemaConstants;
import org.campus.connect.message.utils.AbstractEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "File_tb", schema = SchemaConstants.MESSAGES)
public class File extends AbstractEntity {
  private String name;
  private String type;
  private String url;
  private Long size;
  private String key;
  private UUID uid;
  private Long id_ext;

  public File(MultipartFile file) {
    setName(getNameNoExtension(Objects.requireNonNull(file.getOriginalFilename())));
    setSize(file.getSize());
    setUid(getUid() != null ? getUid() : UUID.randomUUID());
    setType(file.getContentType());
  }

  public String getNameNoExtension(String name) {
    int lastDotIndex = name.lastIndexOf('.');
    if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
      return name.substring(0, lastDotIndex);
    }
    return name;
  }
}
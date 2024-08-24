package org.campus.connect.message.files;


import lombok.*;
import org.campus.connect.message.utils.AbstractEntityDTO;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ArquivoDTO extends AbstractEntityDTO {
  private String name;
  private String type;
  private String url;
  private Long size;
  private String key;
  private UUID uid;
  private Long id_ext;


  public String getUrl() {
    if (getKey() != null) {
      return UrlConstant.getAbsoluteUrl() + "/api/public/arquivo/get/" + getId();
    }
    return null;
  }
}

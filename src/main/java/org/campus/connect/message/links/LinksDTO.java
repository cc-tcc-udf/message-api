package org.campus.connect.message.links;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.utils.AbstractEntityDTO;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LinksDTO extends AbstractEntityDTO {
  private String title;
  private String link;
  private UUID id_msg;
}


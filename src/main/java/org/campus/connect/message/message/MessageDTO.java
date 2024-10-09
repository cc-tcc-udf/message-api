package org.campus.connect.message.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.files.FileDTO;
import org.campus.connect.message.links.LinksDTO;
import org.campus.connect.message.utils.AbstractEntityDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MessageDTO extends AbstractEntityDTO {
  private String title;
  private String summary;
  private LocalDateTime sendDate;
  private Status status;
  private String message;
  private UUID responsible;
  private List<FileDTO> attachments;
  private List<LinksDTO> links;
}

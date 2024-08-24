package org.campus.connect.message.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.campus.connect.message.constants.Enums.Status;
import org.campus.connect.message.files.ArquivoDTO;
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
  private String titulo;
  private String resumo;
  private LocalDateTime data_envio;
  private Status status;
  private String message;
  private UUID resp;
  private List<ArquivoDTO> anexos;
  private List<LinksDTO> links;
}

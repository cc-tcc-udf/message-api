package org.campus.connect.message.utils.dtos;

import lombok.*;
import org.campus.connect.message.constants.GenericMessages;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReturnObjDTO {
  private String message;
  private boolean success;
  private Object data;
  private PageableDTO pageable;

  public ReturnObjDTO(Object data, boolean success) {
    this.data = data;
    this.success = success;
    this.message = success ? GenericMessages.ResponseSuccess : GenericMessages.ResponseError;
  }
}

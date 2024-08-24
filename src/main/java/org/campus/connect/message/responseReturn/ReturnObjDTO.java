package org.campus.connect.message.responseReturn;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReturnObjDTO {
  private String message;
  private boolean success;
  private String type;
  private Object data;
}

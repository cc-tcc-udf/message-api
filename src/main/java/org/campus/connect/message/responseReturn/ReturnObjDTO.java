package org.campus.connect.message.responseReturn;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReturnObjDTO {
  private String message;
  private boolean success;
  private Object data;
}

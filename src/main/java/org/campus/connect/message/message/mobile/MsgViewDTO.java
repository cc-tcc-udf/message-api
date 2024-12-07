package org.campus.connect.message.message.mobile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgViewDTO {
  private UUID user;
  private UUID message;
  private Boolean favorite;
  private Boolean view;
}

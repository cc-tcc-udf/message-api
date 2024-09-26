package org.campus.connect.message.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FirebaseMessageDTO {
  private String userToken;
  private String title;
  private String body;
}

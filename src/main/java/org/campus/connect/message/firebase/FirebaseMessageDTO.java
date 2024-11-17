package org.campus.connect.message.firebase;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class FirebaseMessageDTO {
  private String title;
  private String body;
  private String email;
  private String name;
  private String notificationToken;
}

package org.campus.connect.message.firebase;

import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

  private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

  public String sendNotification(FirebaseMessageDTO dto) {
    try {
//      List<String> tokens = Arrays.asList(dto.getUserToken(), dto.getUserToken2());
//
//      for (String token : tokens) {
      Message message = Message.builder()
        .setToken(dto.getUserToken())
        .setNotification(Notification.builder()
          .setTitle(dto.getTitle())
          .setBody(dto.getBody())
          .build())
        .setAndroidConfig(AndroidConfig.builder()
          .setPriority(AndroidConfig.Priority.HIGH)
          .setNotification(AndroidNotification.builder()
            .setSound("default")
            .build())
          .build())
        .build();


//      }

      return FirebaseMessaging.getInstance().send(message);

    } catch (FirebaseMessagingException e) {
      logger.error("Error sending notification", e);
      return "Erro ao enviar notificação";
    }
  }
}

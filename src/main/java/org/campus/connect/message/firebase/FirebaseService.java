package org.campus.connect.message.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.*;
import org.campus.connect.message.course.CourseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

  private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

  public FirebaseService(final FirebaseApp firebaseApp) {
  }

  public String sendNotification(FirebaseMessageDTO dto) {
    try {
      Message message = Message.builder()
        .setToken(dto.getNotificationToken())
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
      return FirebaseMessaging.getInstance().send(message);

    } catch (FirebaseMessagingException e) {
      logger.error("Error sending notification", e);
      return "Erro ao enviar notificação";
    }
  }

  public List<FirebaseMessageDTO> fechTokens() throws ExecutionException, InterruptedException {
    List<FirebaseMessageDTO> tokens = new ArrayList<>();
    Firestore db = FirestoreClient.getFirestore();

    CollectionReference alunos = db.collection("user");

    ApiFuture<QuerySnapshot> querySnapshot = alunos.get();

    for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
      FirebaseMessageDTO dto = document.toObject(FirebaseMessageDTO.class);
      tokens.add(dto);
    }
    return tokens;
  }

  public List<FirebaseMessageDTO> fetchTokens(String course, String group) throws ExecutionException, InterruptedException {
    List<FirebaseMessageDTO> tokens = new ArrayList<>();
    Firestore db = FirestoreClient.getFirestore();

    CollectionReference alunos = db.collection("groups")
      .document(group)
      .collection("courses")
      .document(course)
      .collection("students");

    ApiFuture<QuerySnapshot> querySnapshot = alunos.get();

    for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
      FirebaseMessageDTO dto = document.toObject(FirebaseMessageDTO.class);
      tokens.add(dto);
    }
    return tokens;
  }

  public void createCollection(final CourseDTO dto, final String group) {
    Firestore db = FirestoreClient.getFirestore();
    db.collection("groups")
      .document(group)
      .collection("courses")
      .document(dto.getName()).set(new HashMap<>());
  }
}

package org.campus.connect.message.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
  private final Resource serviceAccountKeyResource;

  public FirebaseConfig(
    @Value("classpath:secrets/firebase.json") final Resource serviceAccountKeyResource) {
    this.serviceAccountKeyResource = serviceAccountKeyResource;
  }

  @Bean
  public FirebaseApp firebaseApp() throws IOException {
    try (InputStream in = serviceAccountKeyResource.getInputStream()) {
      GoogleCredentials credentials = GoogleCredentials.fromStream(in);
      FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(credentials)
        .build();

      return FirebaseApp.initializeApp(options);
    }
  }
}
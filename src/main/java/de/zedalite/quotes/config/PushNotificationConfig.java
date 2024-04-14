package de.zedalite.quotes.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class PushNotificationConfig {

  @Value("classpath:firebase-account-key.json")
  private Resource resource;

  @Bean
  FirebaseApp firebaseApp() throws IOException {
    final GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());

    final FirebaseOptions options = FirebaseOptions.builder()
      .setCredentials(credentials)
      .build();

    try {
      return FirebaseApp.getInstance();
    } catch (final IllegalStateException ex) {
      return FirebaseApp.initializeApp(options);
    }
  }

  @Bean
  FirebaseMessaging firebaseMessaging(final FirebaseApp firebaseApp) {
    return FirebaseMessaging.getInstance(firebaseApp);
  }
}

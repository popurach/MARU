package com.bird.maru.cloud.firebase.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class FCMInitializer {

    @PostConstruct
    public void initialize() {
        try (InputStream serviceAccount = getClass().getResourceAsStream("/firebase-admin-sdk.json")) {
            FirebaseOptions options = FirebaseOptions.builder()
                                                     .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                                     .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

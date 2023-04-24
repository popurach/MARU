package com.bird.maru.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class FCMInitializer {

    @PostConstruct
    public void initialize() throws IOException {

        InputStream serviceAccount = getClass().getResourceAsStream("/maru-268ba-firebase-adminsdk-kz7ws-cbb0d2f724.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);

    }

}

package com.bird.maru.fcm;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    public String sendMessage() {
        Message.builder()
               .setAndroidConfig(
                       AndroidConfig.builder()
                                    .setNotification(AndroidNotification.builder()
                                                                        .setTitle("Test title")
                                                                        .setBody("Test Body")
                                                                        .build()).build()
               )
               .putData("test", "test")
               .setToken("").build();
        return null;
    }

}

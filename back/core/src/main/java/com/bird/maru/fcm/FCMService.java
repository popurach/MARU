package com.bird.maru.fcm;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    public String sendMessage() throws FirebaseMessagingException {
        final Message message = Message.builder()
                                       .setAndroidConfig(
                                               AndroidConfig.builder()
                                                            .setNotification(AndroidNotification.builder()
                                                                                                .setTitle("Test title")
                                                                                                .setBody("Test Body")
                                                                                                .build()).build()
                                       )
                                       .putData("test", "test")
                                       .setToken(
                                               "dPdBxtyqTN-PFcrOuLQmqn:APA91bHIiatliaUqBgaJ75a46OEhozUI9HoXsgtDXyMTDoGJQhZ098Y12AItEb5zzBLE5EIr6tJJ9IwMOhIwlI8E2LqKQq48LirSdqbAkbk2PgtO0-NKtZIEORquN0R3iZu37MeOdTsG")
                                       .build();

        String response = FirebaseMessaging.getInstance().send(message);
        return response;
    }

}

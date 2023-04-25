package com.bird.maru.fcm;

import com.bird.maru.notice.model.Notice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final ObjectMapper objectMapper;

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

        return FirebaseMessaging.getInstance().send(message);
    }

    public void sendMessage(Notice notice) {
        try {
            Message message = Message.builder()
                                     .setAndroidConfig(
                                             AndroidConfig.builder()
                                                          .setNotification(AndroidNotification.builder()
                                                                                              .setTitle(notice.getCategory().getTitle())
                                                                                              .setBody(notice.getContent())
                                                                                              .build()).build())
                                     .putData("content", objectMapper.writeValueAsString(notice))
                                     .setToken(notice.getNoticeToken())
                                     .build();
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 으로 message 전송 중 에러 발생");
        } catch (JsonProcessingException e) {
            log.error("Notice 를 Json 으로 변환 중 에러 발생");
        }
    }

    public void sendMessageToMembers(List<Notice> notices) {
        for (Notice notice :
                notices) {
            this.sendMessage(notice);
        }
    }

}

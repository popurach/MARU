package com.bird.maru.fcm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FCMServiceTest {

    @Autowired
    private FCMService fcmService;

    @Test
    void sendMessage() throws Exception {
        //given
        fcmService.sendMessage();
        //when

        //then

    }

}
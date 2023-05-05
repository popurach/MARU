package com.bird.maru.cloud.aws.s3.service;

import static org.assertj.core.api.Assertions.*;

import com.bird.maru.domain.model.type.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class AwsS3ServiceTest {

    @Autowired
    private AwsS3Service awsS3Service;

    @Test
    @DisplayName("오브젝트 업로드")
    @Disabled
    void uploadMemberProfile() {
        // given
        MultipartFile image;
        try (InputStream input = new FileInputStream("resources/default_profile.png")) {
            image = new MockMultipartFile("default_profile.png", input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        // when
        Image actual = awsS3Service.uploadMemberProfileImage(image);

        // then
        assertThat(actual.getSavedPath()).startsWith("images/members");
    }

}
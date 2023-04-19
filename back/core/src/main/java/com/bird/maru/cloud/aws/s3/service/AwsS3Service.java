package com.bird.maru.cloud.aws.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bird.maru.domain.model.type.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AwsS3Service {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public AwsS3Service(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    /**
     * 멤버의 프로필 사진을 저장합니다.
     *
     * @param image 저장할 1개의 프로필 사진
     * @return Image 객체를 반환합니다. 이 객체는 {@code S3Object}에 대한 기본 정보(저장 경로, URL)를 담고 있습니다.
     */
    public Image uploadMemberProfile(MultipartFile image) {
        return upload(image, "images/members");
    }

    /**
     * 스팟 사진을 저장합니다.
     * @param image 저장할 1개의 스팟 사진
     * @return Image 객체를 반환합니다. 이 객체는 {@code S3Object}에 대한 기본 정보(저장 경로, URL)를 담고 있습니다.
     */
    public Image uploadSpotImage(MultipartFile image) {
        return upload(image, "images/spots");
    }

    private Image upload(MultipartFile image, String path) {
        String key = createKey(path);

        try (InputStream input = image.getInputStream()) {
            amazonS3.putObject(
                    new PutObjectRequest(
                            this.bucketName, key, input, getObjectMetadata(image)
                    ).withCannedAcl(CannedAccessControlList.PublicRead)
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("업로드 하려는 파일을 읽을 수 없습니다.", e);
        }

        return Image.builder()
                    .savedPath(key)
                    .imageUrl(amazonS3.getUrl(this.bucketName, key))
                    .build();
    }

    private String createKey(String path) {
        return path + "/" + UUID.randomUUID();
    }

    private ObjectMetadata getObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());
        return metadata;
    }


}

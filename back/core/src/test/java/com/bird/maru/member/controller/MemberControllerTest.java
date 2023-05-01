package com.bird.maru.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bird.maru.cloud.aws.s3.service.AwsS3Service;
import com.bird.maru.common.config.MockMvcConfig;
import com.bird.maru.common.util.JwtUtil;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.auth.service.dto.CustomUserDetails;
import com.bird.maru.domain.model.type.Provider;
import com.bird.maru.member.controller.dto.MemberInfoDto;
import com.bird.maru.member.mapper.MemberMapper;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.member.repository.query.MemberQueryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Import({ MockMvcConfig.class })
class MemberControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberMapper mapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberQueryRepository memberQueryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AwsS3Service awsS3Service;

    @BeforeEach
    void beforeEach() {
        Member testMember = Member.builder()
                                  .nickname("test")
                                  .email("test@naver.com")
                                  .provider(Provider.NAVER)
                                  .build();

        memberRepository.saveAndFlush(testMember);
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 기본 정보 조회 테스트")
    void getMemberInfoTest() throws Exception {
        // given
        Member testMember = memberQueryRepository.findAll().get(0);
        String testToken = jwtUtil.generateRefreshToken(
                CustomUserDetails.builder()
                                 .id(testMember.getId())
                                 .nickname(testMember.getNickname())
                                 .provider(testMember.getProvider())
                                 .email(testMember.getEmail())
                                 .build()
        );

        // when
        String content = mockMvc.perform(
                                        get("/api/members/my")
                                                .header("Authorization", "Bearer " + testToken)
                                )
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

        // then
        MemberInfoDto actual = objectMapper.readValue(content, MemberInfoDto.class);
        assertThat(actual).isEqualTo(mapper.toMemberInfoDto(testMember));
    }

    @Test
    @DisplayName("Notice Token 등록 테스트")
    void registerNoticeTokenTest() throws Exception {
        // given
        Member testMember = memberQueryRepository.findAll().get(0);
        String testAccessToken = jwtUtil.generateAccessToken(
                CustomUserDetails.builder()
                                 .id(testMember.getId())
                                 .nickname(testMember.getNickname())
                                 .provider(testMember.getProvider())
                                 .email(testMember.getEmail())
                                 .build()
        );
        String testNoticeToken = "tokenotkemkdsiidkkensiaos";

        // when
        mockMvc.perform(
                post("/api/members/notice-token")
                        .header("Authorization", "Bearer " + testAccessToken)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(testNoticeToken)
        ).andExpect(status().isOk());

        // then
        testMember = memberQueryRepository.findAll().get(0);
        assertThat(testMember.getNoticeToken()).isEqualTo(testNoticeToken);
    }

    @Test
    @DisplayName("회원 기본 정보 수정 테스트")
    void modifyMemberInfoTest() throws Exception {
        // given
        Member testMember = memberQueryRepository.findAll().get(0);
        String testToken = jwtUtil.generateRefreshToken(
                CustomUserDetails.builder()
                                 .id(testMember.getId())
                                 .nickname(testMember.getNickname())
                                 .provider(testMember.getProvider())
                                 .email(testMember.getEmail())
                                 .build()
        );

        MockMultipartFile image;
        try (InputStream input = new FileInputStream("resources/test_profile.png")) {
            image = new MockMultipartFile("image", "test_profile.png", "image/png", input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        // when
        String content = mockMvc.perform(
                                        multipart("/api/members/my")
                                                .file(image)
                                                .header("Authorization", "Bearer " + testToken)
                                                .content("modified")
                                )
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

        // then
        MemberInfoDto actual = objectMapper.readValue(content, MemberInfoDto.class);
        testMember = memberQueryRepository.findAll().get(0);
        assertThat(actual).isEqualTo(mapper.toMemberInfoDto(testMember));
        awsS3Service.deleteFile(testMember.getImage().getSavedPath());
    }

}
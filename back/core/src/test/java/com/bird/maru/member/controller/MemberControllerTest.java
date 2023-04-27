package com.bird.maru.member.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.bird.maru.common.config.MockMvcConfig;
import com.bird.maru.common.util.JwtUtil;
import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.type.CustomUserDetails;
import com.bird.maru.domain.model.type.Provider;
import com.bird.maru.member.controller.dto.MemberInfoDto;
import com.bird.maru.member.mapper.MemberMapper;
import com.bird.maru.member.repository.MemberRepository;
import com.bird.maru.member.repository.query.MemberQueryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
        Member testMember = memberQueryRepository.findAll(Pageable.ofSize(1)).get().findFirst().get();
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
                                        MockMvcRequestBuilders.get("/api/members/my")
                                                              .header("Authorization", "Bearer " + testToken)
                                )
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

        // then
        MemberInfoDto actual = objectMapper.readValue(content, MemberInfoDto.class);
        assertThat(actual).isEqualTo(mapper.toMemberInfoDto(testMember));
    }

}
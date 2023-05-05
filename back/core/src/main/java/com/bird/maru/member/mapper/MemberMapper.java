package com.bird.maru.member.mapper;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.landmark.controller.dto.OwnerResponseDto;
import com.bird.maru.member.controller.dto.MemberInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(expression = "java(member.getImage().getUrl().toString())", target = "imageUrl")
    MemberInfoDto toMemberInfoDto(Member member);

    @Mapping(expression = "java(member.getId())", target = "id")
    @Mapping(expression = "java(member.getNickname())", target = "nickname")
    @Mapping(expression = "java(member.getImage().getUrl().toString())", target = "profileImageUrl")
    @Mapping(expression = "java(url)", target = "spotImageUrl")
    OwnerResponseDto toOwnerResponseDto(Member member, String url);

}

package com.bird.maru.member.mapper;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.member.controller.dto.MemberInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(expression = "java(member.getImage().getUrl().toString())", target = "imageUrl")
    MemberInfoDto toMemberInfoDto(Member member);

}

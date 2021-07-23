package io.seoul.helper.controller.member.dto;

import io.seoul.helper.domain.member.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRequestDto {
    private Long teamId;
    private MemberRole role;

    @Builder
    public MemberRequestDto(Long teamId, MemberRole role) {
        this.teamId = teamId;
        this.role = role;
    }
}

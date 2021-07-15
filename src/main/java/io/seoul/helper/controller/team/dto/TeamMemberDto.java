package io.seoul.helper.controller.team.dto;

import io.seoul.helper.domain.member.Member;
import lombok.Getter;

@Getter
public class TeamMemberDto {
    private String nickname;
    private String memberRole;
    private Boolean Creator;

    public TeamMemberDto(Member member) {
        this.nickname = member.getUser().getNickname();
        this.memberRole = member.getRole().getName();
        this.Creator = member.getCreator();
    }
}

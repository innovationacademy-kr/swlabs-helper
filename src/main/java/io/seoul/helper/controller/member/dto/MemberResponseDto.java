package io.seoul.helper.controller.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private Long userId;
    private String nickname;
    private String memberRole;
    private Boolean creator;
    private Boolean participation;
    private String picture;

    @Builder
    public MemberResponseDto(Long id, Long userId, String nickname, String memberRole,
                             Boolean creator, Boolean participation, String picture) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.memberRole = memberRole;
        this.creator = creator;
        this.participation = participation;
        this.picture = picture;
    }
}

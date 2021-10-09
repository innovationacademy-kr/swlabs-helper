package io.seoul.helper.controller.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private String picture;

    @Builder
    public UserResponseDto(Long id, String name, String email, String nickname, String picture) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
    }
}

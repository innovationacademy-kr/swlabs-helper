package io.seoul.helper.controller.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private String name;
    private String email;
    private String nickname;

    @Builder
    public UserResponseDto(String name, String email, String nickname) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }
}

package io.seoul.helper.controller.user;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.dto.ResultResponseDto;
import io.seoul.helper.controller.user.dto.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {

    @GetMapping(value = "/api/v1/user/current")
    public ResultResponseDto getCurrentUser(@LoginUser SessionUser sessionUser) {
        UserResponseDto user = null;
        if (sessionUser != null)
            user = UserResponseDto.builder()
                    .nickname(sessionUser.getNickname())
                    .name(sessionUser.getNickname())
                    .email(sessionUser.getEmail())
                    .build();
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .data(user)
                .build();
    }
}

package io.seoul.helper.controller.user;

import io.seoul.helper.config.aop.ApiControllerTryCatch;
import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.dto.ResultResponseDto;
import io.seoul.helper.controller.user.dto.UserResponseDto;
import io.seoul.helper.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserApiController {
    private final UserService userService;

    @GetMapping(value = "/api/v1/user/current")
    @ApiControllerTryCatch
    public ResultResponseDto getCurrentUser(@LoginUser SessionUser sessionUser) throws Throwable {
        UserResponseDto user = userService.findUserBySession(sessionUser);
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .data(user)
                .build();
    }
}

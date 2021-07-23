package io.seoul.helper.controller.member;

import io.seoul.helper.config.aop.ApiControllerTryCatch;
import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.dto.ResultResponseDto;
import io.seoul.helper.controller.member.dto.MemberRequestDto;
import io.seoul.helper.service.MailSenderService;
import io.seoul.helper.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class MemberApiController {
    private final MemberService memberService;
    private final MailSenderService mailSenderService;

    @ApiControllerTryCatch
    @PostMapping(value = "/api/v1/member")
    public ResultResponseDto joinTeam(@LoginUser SessionUser user,
                                      @RequestBody MemberRequestDto requestDto) throws Exception {
        memberService.joinTeam(user, requestDto);
        mailSenderService.sendJoinMail(user, requestDto.getTeamId(), "helper.42seoul.io");
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(null)
                .build();
    }

    @ApiControllerTryCatch
    @DeleteMapping(value = "/api/v1/member")
    public ResultResponseDto outTeam(@LoginUser SessionUser user,
                                     @RequestBody MemberRequestDto requestDto) throws Exception {
        memberService.outTeam(user, requestDto);
        mailSenderService.sendOutMail(user, requestDto.getTeamId(), "helper.42seoul.io");
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(null)
                .build();
    }
}

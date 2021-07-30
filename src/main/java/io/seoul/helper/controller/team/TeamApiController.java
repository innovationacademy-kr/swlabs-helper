package io.seoul.helper.controller.team;

import io.seoul.helper.config.aop.ApiControllerTryCatch;
import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.dto.ResultResponseDto;
import io.seoul.helper.controller.team.dto.*;
import io.seoul.helper.service.MailSenderService;
import io.seoul.helper.service.ReviewService;
import io.seoul.helper.service.TeamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class TeamApiController {
    private final TeamService teamService;
    private final ReviewService reviewService;
    private final MailSenderService mailSenderService;

    @ApiControllerTryCatch
    @PostMapping(value = "/api/v1/team")
    public ResultResponseDto create(@LoginUser SessionUser user, @RequestBody TeamCreateRequestDto requestDto) throws Exception {
        TeamResponseDto data = teamService.createNewTeam(user, requestDto);
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(data)
                .build();
    }

    @ApiControllerTryCatch
    @GetMapping(value = "/api/v1/teams")
    public ResultResponseDto teamList(@ModelAttribute TeamListRequestDto requestDto) throws EntityNotFoundException {
        Page<TeamResponseDto> teams = teamService.findTeams(requestDto);
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(teams)
                .build();
    }

    @ApiControllerTryCatch
    @GetMapping(value = "/api/v1/team/{id}")
    public ResultResponseDto getTeam(@PathVariable Long id) throws EntityNotFoundException {
        TeamResponseDto data = teamService.findTeam(id);
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .data(data)
                .build();
    }

    @ApiControllerTryCatch
    @GetMapping(value = "/api/v1/team/locations")
    public ResultResponseDto teamLocationList() throws Exception {
        List<TeamLocationDto> locations = teamService.findAllLocation();
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(locations)
                .build();
    }

    @ApiControllerTryCatch
    @PutMapping(value = "/api/v1/team/{id}")
    public ResultResponseDto update(@LoginUser SessionUser user,
                                    @PathVariable Long id,
                                    @RequestBody TeamUpdateRequestDto requestDto) throws Exception {
        TeamResponseDto data = teamService.updateTeamByMentor(user, id, requestDto);
        mailSenderService.sendMatchMail(data.getTeamId(), "helper.42seoul.io");
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(data)
                .build();
    }

    @ApiControllerTryCatch
    @PostMapping(value = "api/v1/team/review")
    public ResultResponseDto reviewTeam(@LoginUser SessionUser user,
                                        @RequestBody TeamReviewRequestDto requestDto) throws Exception {
        teamService.reviewTeam(user, requestDto);
        mailSenderService.sendEndMail(requestDto.getId(), "helper.42seoul.io");
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(null)
                .build();
    }

    @ApiControllerTryCatch
    @PostMapping(value = "/api/v1/team/{id}/revoke")
    public ResultResponseDto revokeTeam(@LoginUser SessionUser user, @PathVariable Long id) throws Exception {
        teamService.revokeTeam(user, id);
        mailSenderService.sendEndMail(id, "helper.42seoul.io");
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(null)
                .build();
    }
}

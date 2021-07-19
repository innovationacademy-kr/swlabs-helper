package io.seoul.helper.controller.team;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.dto.ResultResponseDto;
import io.seoul.helper.controller.team.dto.*;
import io.seoul.helper.service.MailSenderService;
import io.seoul.helper.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class TeamApiController {
    @Autowired
    TeamService teamService;

    @Autowired
    MailSenderService mailSenderService;

    @PostMapping(value = "/api/v1/team")
    public ResultResponseDto create(@LoginUser SessionUser user, @RequestBody TeamCreateRequestDto requestDto) {
        TeamResponseDto data;
        try {
            data = teamService.createNewTeam(user, requestDto);
        } catch (Exception e) {
            log.error("failed to create new teamWish : " + e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(data)
                .build();
    }

    @GetMapping(value = "/api/v1/teams")
    public ResultResponseDto teamList(@ModelAttribute TeamListRequestDto requestDto) {
        try {
            Page<TeamResponseDto> teams = teamService.findTeams(requestDto);
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("OK")
                    .data(teams)
                    .build();
        } catch (Exception e) {
            log.error("fail to find team list" + e.getMessage());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @GetMapping(value = "/api/v1/team/{id}")
    public ResultResponseDto getTeam(@PathVariable Long id) {
        TeamResponseDto data;
        try {
            data = new TeamResponseDto(teamService.findTeam(id));
        } catch (Exception e) {
            log.error("failed to get team : " + e.getMessage() + "\n\n" + e.getCause());
            log.error(e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(e.getMessage())
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .data(data)
                .build();
    }

    @GetMapping(value = "/api/v1/team/locations")
    public ResultResponseDto teamLocationList() {
        try {
            List<TeamLocationDto> locations = teamService.findAllLocation();
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("OK")
                    .data(locations)
                    .build();
        } catch (Exception e) {
            return ResultResponseDto.builder()
                    .statusCode((HttpStatus.BAD_REQUEST.value()))
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @PutMapping(value = "/api/v1/team/{id}")
    public ResultResponseDto update(HttpServletRequest request,
                                    @LoginUser SessionUser user,
                                    @PathVariable Long id,
                                    @RequestBody TeamUpdateRequestDto requestDto) {
        TeamResponseDto data;
        try {
            data = teamService.updateTeamByMentor(user, id, requestDto);
            mailSenderService.sendMatchMail(data.getTeamId(), request.getLocalName());
        } catch (Exception e) {
            log.error("failed to update teamWish : " + e.getMessage() + "\n\n" + e.getCause());
            log.error(e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(e.getMessage())
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(data)
                .build();
    }

    @DeleteMapping(value = "/api/v1/team/{id}")
    public ResultResponseDto delete(@LoginUser SessionUser user, @PathVariable Long id) {
        try {
            teamService.deleteTeam(user, id);
        } catch (Exception e) {
            log.error("failed to delete team : " + e.getMessage() + "\n\n" + e.getCause());
            log.error(e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(null)
                .build();
    }

    @PostMapping(value = "/api/v1/team/{id}/join")
    public ResultResponseDto joinTeam(HttpServletRequest request,
                                      @LoginUser SessionUser user, @PathVariable Long id) {
        try {
            teamService.joinTeam(user, id);
            mailSenderService.sendJoinMail(user, id, request.getLocalName());
        } catch (Exception e) {
            log.error("failed to join team : " + e.getMessage() + "\n\n" + e.getCause());
            log.error(e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(null)
                .build();
    }

    @PostMapping(value = "/api/v1/team/{id}/end")
    public ResultResponseDto endTeam(HttpServletRequest request,
                                     @LoginUser SessionUser user, @PathVariable Long id) {
        try {
            teamService.endTeam(user, id);
            mailSenderService.sendEndMail(id, request.getLocalName());
        } catch (Exception e) {
            log.error("failed to end team : " + e.getMessage() + "\n\n" + e.getCause());
            log.error(e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(null)
                .build();
    }

    @DeleteMapping(value = "/api/v1/team/{id}/out")
    public ResultResponseDto outTeam(HttpServletRequest request,
                                     @LoginUser SessionUser user, @PathVariable Long id) {
        try {
            teamService.outTeam(user, id);
            mailSenderService.sendOutMail(user, id, request.getLocalName());
        } catch (Exception e) {
            log.error("failed to out team : " + e.getMessage() + "\n\n" + e.getCause());
            log.error(e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(null)
                .build();
    }
}

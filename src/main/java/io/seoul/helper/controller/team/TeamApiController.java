package io.seoul.helper.controller.team;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.dto.ResultResponseDto;
import io.seoul.helper.controller.team.dto.TeamCreateRequestDto;
import io.seoul.helper.controller.team.dto.TeamListRequestDto;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
import io.seoul.helper.controller.team.dto.TeamUpdateRequestDto;
import io.seoul.helper.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TeamApiController {
    @Autowired
    TeamService teamService;

    @PostMapping(value = "/api/v1/team")
    public Long create(@LoginUser SessionUser user, @RequestBody TeamCreateRequestDto requestDto) {
        try {
            return teamService.createNewTeamWish(user, requestDto);
        } catch (Exception e) {
            log.error("failed to create new teamWish : " + e.getMessage() + "\n\n" + e.getCause());
            return -1L;
        }
    }

    @GetMapping(value = "/api/v1/teams")
    public ResultResponseDto teamList(@ModelAttribute TeamListRequestDto requestDto) {
        //todo: startTime 와 endTime 의 시간 유효성 Field 검증 로직 필요
        List<TeamResponseDto> teams = teamService.findTeams(requestDto);

        log.info("offset={}, limit={}, startTime={}, endTime={}, status={}, location={}",
                requestDto.getOffset(), requestDto.getLimit(), requestDto.getStartTime(),
                requestDto.getEndTime(), requestDto.getStatus(), requestDto.getLocation());

        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .count((teams == null ? 0 : teams.size()))
                .data(teams)
                .build();
    }

    @PutMapping(value = "/api/v1/team/{id}")
    public ResultResponseDto update(@LoginUser SessionUser user,
                                    @PathVariable Long id,
                                    @RequestBody TeamUpdateRequestDto requestDto) {
        TeamResponseDto data;
        try {
            data = teamService.updateTeamByMentor(user, id, requestDto);
        } catch (Exception e) {
            log.error("failed to update teamWish : " + e.getMessage() + "\n\n" + e.getCause());
            log.error(e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .count(0)
                    .data(e.getMessage())
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .count(1)
                .data(data)
                .build();
    }

    @DeleteMapping(value = "/api/v1/team/{id}")
    public ResultResponseDto delete(@LoginUser SessionUser user,
                                    @PathVariable Long id) {
        try {
            teamService.deleteTeam(user, id);
        } catch (Exception e) {
            log.error("failed to delete team : " + e.getMessage() + "\n\n" + e.getCause());
            log.error(e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .count(0)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .count(0)
                .data(null)
                .build();
    }

    @PostMapping(value = "/api/v1/team/{id}/join")
    public ResultResponseDto joinTeam(@LoginUser SessionUser user, @PathVariable Long id) {
        try {
            teamService.joinTeam(user, id);
        } catch (Exception e) {
            log.error("failed to join team : " + e.getMessage() + "\n\n" + e.getCause());
            log.error(e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .count(0)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .count(0)
                .data(null)
                .build();
    }

    @DeleteMapping(value = "/api/v1/team/{id}/out")
    public ResultResponseDto outTeam(@LoginUser SessionUser user, @PathVariable Long id) {
        try {
            teamService.outTeam(user, id);
        } catch (Exception e) {
            log.error("failed to out team : " + e.getMessage() + "\n\n" + e.getCause());
            log.error(e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .count(0)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .count(0)
                .data(null)
                .build();
    }
}

package io.seoul.helper.controller.team;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.dto.ResultResponseDto;
import io.seoul.helper.controller.team.dto.TeamCreateRequestDto;
import io.seoul.helper.controller.team.dto.TeamListRequestDto;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
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

    @PostMapping(value = "/api/v1/teams")
    public Long create(@LoginUser SessionUser user, @RequestBody TeamCreateRequestDto requestDto) {
        try {
            return teamService.createNewTeamWish(user, requestDto);
        } catch (Exception e) {
            log.error("failed to create new teamwish : " + e.getMessage());
            return -1L;
        }
    }

    @GetMapping(value = "/api/v1/teams")
    public ResultResponseDto teamList(@ModelAttribute TeamListRequestDto requestDto) {
        try {
            List<TeamResponseDto> teams = teamService.findTeams(requestDto);
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.OK.value())
                    .count(teams == null ? 0 : teams.size())
                    .data(teams)
                    .build();
        } catch (Exception e) {
            log.error("fail to find team list" + e.getMessage());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }
}

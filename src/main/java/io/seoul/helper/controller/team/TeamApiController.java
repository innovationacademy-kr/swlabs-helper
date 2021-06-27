package io.seoul.helper.controller.team;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.team.dto.TeamCreateRequestDto;
import io.seoul.helper.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TeamApiController {
    @Autowired
    TeamService teamService;

    @PostMapping(value = "/api/v1/create_team")
    public Long create(@LoginUser SessionUser user, @RequestBody TeamCreateRequestDto requestDto) {
        try {
            return teamService.createNewTeamWish(user, requestDto);
        } catch (Exception e) {
            log.error("failed to create new teamwish : " + e.getMessage());
            return -1L;
        }
    }
}

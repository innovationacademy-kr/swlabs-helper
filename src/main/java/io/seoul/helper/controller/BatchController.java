package io.seoul.helper.controller;

import io.seoul.helper.controller.dto.ResultResponseDto;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
import io.seoul.helper.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchController {

    private TeamService teamService;

    @PostMapping(value = "/api/v1/batch/teams/status")
    public ResultResponseDto updateTeamStatus() {
        List<TeamResponseDto> data;
        try {
            data = teamService.updateTeamsLessThanCurrentTime();
        } catch (EntityNotFoundException e) {
            log.info("not found team status : " + e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        } catch (Exception e) {
            log.error("failed to update team status : " + e.getMessage() + "\n\n" + e.getCause());
            return ResultResponseDto.builder()
                    .statusCode(HttpStatus.OK.value())
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
}

package io.seoul.helper.controller;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.team.dto.TeamCountRequestDto;
import io.seoul.helper.controller.team.dto.TeamCountResponseDto;
import io.seoul.helper.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class PageAdminController {
    private TeamService teamService;

    @GetMapping("index")
    public String home(Model model, @LoginUser SessionUser user) {
        if (user == null)
            return "index";

        LocalDateTime start = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusMonths(1).with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        TeamCountRequestDto teamCountRequestDto = TeamCountRequestDto.builder()
                .start(start)
                .end(end)
                .build();

        TeamCountResponseDto responseDto = teamService.getTeamCount(teamCountRequestDto);
        model.addAttribute("user", user);
        model.addAttribute("teamCount", responseDto);
        model.addAttribute("today", start.format(DateTimeFormatter.ofPattern("yyyy.MM")));
        return "admin/index";
    }

    @GetMapping("settle")
    public String settle(Model model, @LoginUser SessionUser user) {
        if (user == null)
            return "index";
        model.addAttribute("user", user);
        return "admin/settle";
    }

    @GetMapping("settle_history")
    public String settleHistory(Model model, @LoginUser SessionUser user) {
        if (user == null)
            return "index";
        model.addAttribute("user", user);
        return "admin/settle_history";
    }
}
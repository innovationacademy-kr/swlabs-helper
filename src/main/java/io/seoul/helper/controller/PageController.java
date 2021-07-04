package io.seoul.helper.controller;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.team.dto.TeamListRequestDto;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PageController {
    @Autowired
    private TeamService teamService;

    @GetMapping(value = "/")
    public String home(Model model, @LoginUser SessionUser user) {
        if (user != null) {
            model.addAttribute("userNickname", user.getNickname());
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping(value = "/list_team")
    public String teamList(Model model, @LoginUser SessionUser user) {
        TeamListRequestDto dto = new TeamListRequestDto();
        List<TeamResponseDto> teams = teamService.findTeams(dto);
        model.addAttribute("teams", teams);
        model.addAttribute("user", user);
        return "list_team";
    }

    @GetMapping(value = "/list_myteam")
    public String myTeamList(Model model, @LoginUser SessionUser user) {
        TeamListRequestDto dto = new TeamListRequestDto();
        dto.setUserNickname(user.getNickname());
        List<TeamResponseDto> teams = teamService.findTeams(dto);
        model.addAttribute("teams", teams);
        model.addAttribute("user", user);
        return "list_myteam";
    }

    @GetMapping(value = "/set_time")
    public String time() {
        return "set_time";
    }

    @GetMapping(value = "/create_team")
    public String createTeam(Model model, @LoginUser SessionUser user,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page) {

        TeamStatus status = TeamStatus.WAITING;

        TeamListRequestDto dto = new TeamListRequestDto();
        dto.setStatus(status);
        List<TeamResponseDto> teams = teamService.findTeams(dto);

        model.addAttribute("teams", teams);
        model.addAttribute("user", user);
        return "create_team";
    }
}
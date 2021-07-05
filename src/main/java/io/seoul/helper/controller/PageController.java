package io.seoul.helper.controller;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.team.dto.TeamListRequestDto;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    @Autowired
    private TeamService teamService;

    @GetMapping(value = "/")
    public String home(Model model, @LoginUser SessionUser user) {
        if (user == null) {
            TeamListRequestDto dto = new TeamListRequestDto();
            Page<TeamResponseDto> teams = teamService.findTeams(dto);
            model.addAttribute("teams", teams);
        }
        if (user != null) {
            TeamListRequestDto allTeamDto = new TeamListRequestDto();
            Page<TeamResponseDto> allTeams = teamService.findTeams(allTeamDto);
            model.addAttribute("allTeams", allTeams);

            TeamListRequestDto myTeamDto = new TeamListRequestDto();
            myTeamDto.setUserNickname(user.getNickname());
            Page<TeamResponseDto> myTeams = teamService.findTeams(myTeamDto);
            model.addAttribute("myTeams", myTeams);

            model.addAttribute("userNickname", user.getNickname());
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping(value = "/list_team")
    public String teamList(Model model, @LoginUser SessionUser user,
                           @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        TeamListRequestDto dto = new TeamListRequestDto();
        dto.setOffset(offset);
        Page<TeamResponseDto> teams = teamService.findTeams(dto);

        model.addAttribute("teams", teams);
        model.addAttribute("user", user);

        return "list_team";
    }

    @GetMapping(value = "/list_myteam")
    public String myTeamList(Model model, @LoginUser SessionUser user,
                             @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        TeamListRequestDto dto = new TeamListRequestDto();
        dto.setUserNickname(user.getNickname());
        dto.setOffset(offset);
        Page<TeamResponseDto> teams = teamService.findTeams(dto);

        model.addAttribute("teams", teams);
        model.addAttribute("user", user);

        return "list_myteam";
    }

    @GetMapping(value = "/set_time")
    public String time(Model model, @LoginUser SessionUser user) {
        if (user != null) {
            model.addAttribute("userNickname", user.getNickname());
            model.addAttribute("user", user);
        }
        return "set_time";
    }

    @GetMapping(value = "/create_team")
    public String createTeam(Model model, @LoginUser SessionUser user,
                             @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {

        TeamStatus status = TeamStatus.WAITING;

        TeamListRequestDto dto = new TeamListRequestDto();
        dto.setStatus(status);
        dto.setOffset(offset);
        Page<TeamResponseDto> teams = teamService.findTeams(dto);

        model.addAttribute("teams", teams);
        model.addAttribute("user", user);

        return "create_team";
    }
}
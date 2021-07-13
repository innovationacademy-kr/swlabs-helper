package io.seoul.helper.controller;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.team.dto.TeamListRequestDto;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.service.ProjectService;
import io.seoul.helper.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class PageController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private ProjectService projectService;

    @GetMapping(value = "/")
    public String home(Model model, @LoginUser SessionUser user) {
        if (user == null) {
            TeamListRequestDto dto = new TeamListRequestDto();
            dto.setStartTimePrevious(LocalDateTime.now());
            Page<TeamResponseDto> teams = teamService.findTeams(dto);
            model.addAttribute("teams", teams);
        }
        if (user != null) {
            TeamListRequestDto allTeamDto = new TeamListRequestDto();
            allTeamDto.setStartTimePrevious(LocalDateTime.now());
            allTeamDto.setExcludeNickname(user.getNickname());
            Page<TeamResponseDto> allTeams = teamService.findTeams(allTeamDto);
            model.addAttribute("allTeams", allTeams);

            TeamListRequestDto myTeamDto = new TeamListRequestDto();
            myTeamDto.setEndTimePrevious(LocalDateTime.now());
            myTeamDto.setNickname(user.getNickname());
            Page<TeamResponseDto> myTeams = teamService.findTeams(myTeamDto);
            model.addAttribute("myTeams", myTeams);

            model.addAttribute("userNickname", user.getNickname());
            model.addAttribute("user", user);
            model.addAttribute("projects", projectService.findAllProjects());
            model.addAttribute("locations", teamService.findAllLocation());
        }
        return "index";
    }

    @GetMapping(value = "/list_team")
    public String teamList(Model model, @LoginUser SessionUser user,
                           @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        TeamListRequestDto dto = new TeamListRequestDto();
        dto.setOffset(offset);
        dto.setStartTimePrevious(LocalDateTime.now());
        dto.setExcludeNickname(user.getNickname());
        Page<TeamResponseDto> teams = teamService.findTeams(dto);

        model.addAttribute("teams", teams);
        model.addAttribute("user", user);
        model.addAttribute("projects", projectService.findAllProjects());
        model.addAttribute("locations", teamService.findAllLocation());

        return "list_team";
    }

    @GetMapping(value = "/list_myteam")
    public String myTeamList(Model model, @LoginUser SessionUser user,
                             @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        TeamListRequestDto dto = new TeamListRequestDto();
        dto.setNickname(user.getNickname());
        dto.setOffset(offset);
        dto.setEndTimePrevious(LocalDateTime.now());
        Page<TeamResponseDto> teams = teamService.findTeams(dto);

        model.addAttribute("teams", teams);
        model.addAttribute("user", user);
        model.addAttribute("projects", projectService.findAllProjects());
        model.addAttribute("locations", teamService.findAllLocation());

        return "list_myteam";
    }

    @GetMapping(value = "/mentee")
    public String time(Model model, @LoginUser SessionUser user,
                       @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        if (user != null) {
            model.addAttribute("userNickname", user.getNickname());
            model.addAttribute("user", user);
        }
        TeamListRequestDto dto = new TeamListRequestDto();
        dto.setNickname(user.getNickname());
        dto.setCreateor(true);
        dto.setOffset(offset);
        dto.setStatus(TeamStatus.WAITING);
        dto.setEndTimePrevious(LocalDateTime.now());
        Page<TeamResponseDto> teams = teamService.findTeams(dto);
        model.addAttribute("teams", teams);
        model.addAttribute("projects", projectService.findAllProjects());
        model.addAttribute("locations", teamService.findAllLocation());
        return "mentee";
    }

    @GetMapping(value = "/mentor")
    public String createTeam(Model model, @LoginUser SessionUser user,
                             @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {

        TeamStatus status = TeamStatus.WAITING;

        TeamListRequestDto dto = new TeamListRequestDto();
        dto.setStatus(status);
        dto.setOffset(offset);
        dto.setStartTimePrevious(LocalDateTime.now());
        dto.setExcludeNickname(user.getNickname());
        Page<TeamResponseDto> teams = teamService.findTeams(dto);

        model.addAttribute("teams", teams);
        model.addAttribute("user", user);
        model.addAttribute("projects", projectService.findAllProjects());
        model.addAttribute("locations", teamService.findAllLocation());

        return "mentor";
    }
}
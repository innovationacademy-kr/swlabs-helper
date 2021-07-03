package io.seoul.helper.controller;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;

import io.seoul.helper.controller.team.dto.TeamListRequestDto;
import io.seoul.helper.service.TeamService;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.repository.team.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PageController {
    @Autowired
    private TeamRepository teamRepo;

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
    public String createTeam(Model model,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                             @RequestParam(value = "size", required = false, defaultValue = "5") int size) {

        TeamStatus status = TeamStatus.WAITING;
        Page<Team> teamPage = teamRepo.findAllByStatus(status, PageRequest.of(page - 1, size));
        Page<TeamResponseDto> teams = teamPage.map(team -> new TeamResponseDto(team));

        model.addAttribute("teams", teams);

        int totalPages = teams.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "create_team";
    }



}
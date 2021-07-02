package io.seoul.helper.controller;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
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

@Controller
public class PageController {
    @Autowired
    private TeamRepository teamRepo;

    @GetMapping(value = "/")
    public String home(Model model, @LoginUser SessionUser user) {
        if (user != null) {
            model.addAttribute("userNickname", user.getNickname());
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping(value = "/list_team")
    public String teamList() {
        return "list_team";
    }

    @GetMapping(value = "/set_time")
    public String time() {
        return "set_time";
    }

    @GetMapping(value = "/create_team")
    public String createTeam(Model model,
                             @RequestParam(value = "page", required = false, defaultValue = "0") int page) {
        TeamStatus status = TeamStatus.WAITING;

        Page<Team> teamPage = teamRepo.findAllByStatus(status, PageRequest.of(page, 2));
        Page<TeamResponseDto> teams = teamPage.map(team -> new TeamResponseDto(team));

        model.addAttribute("pages", teams);
        model.addAttribute("maxPage", 3);
        model.addAttribute("teams", teams);

        return "create_team";
    }
}
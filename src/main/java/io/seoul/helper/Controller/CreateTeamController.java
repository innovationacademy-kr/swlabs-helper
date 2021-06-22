package io.seoul.helper.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateTeamController {

    @GetMapping(value = "/create")
    public String createTeam(){
        return "create_team";
    }
}

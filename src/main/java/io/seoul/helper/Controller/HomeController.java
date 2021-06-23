package io.seoul.helper.Controller;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.domain.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

   @GetMapping(value = "/team-list")
   public String teamList() {
      return "team-list";
   }

   @GetMapping(value = "/")
   public String home(Model model, @LoginUser SessionUser user) {
      if (user != null) {
         model.addAttribute("userNickname", user.getNickname());
         model.addAttribute("user", user);
      }
      return "index";
   }

   @GetMapping(value = "/set-time")
   public String time() {
      return "set-time";
   }
}

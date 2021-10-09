package io.seoul.helper.controller;

import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.review.dto.ReviewNeedSettleResponseDto;
import io.seoul.helper.controller.settle.dto.SettleNeedPaidGroupByUserResponseDto;
import io.seoul.helper.service.ReviewService;
import io.seoul.helper.service.SettleService;
import io.seoul.helper.service.TeamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class PageAdminController {
    private final TeamService teamService;
    private final ReviewService reviewService;
    private final SettleService settleService;

    @GetMapping("settle")
    public String settle(Model model, @LoginUser SessionUser user, @RequestParam(defaultValue = "10") int limit) {
        if (user == null)
            return "index";
        List<ReviewNeedSettleResponseDto> reviews = null;
        Long leftCount = 0L;
        try {
            reviews = reviewService.findReviewsNotSettle(user, 10);
            leftCount = reviewService.getReviewNeedSettleCount().getCount();

        } catch (Exception e) {
            log.error("failed to read data from settle : " + e.getMessage());
        }
        model.addAttribute("user", user);
        model.addAttribute("reviews", reviews);
        model.addAttribute("leftCount", leftCount - reviews.size());
        return "admin/settle";
    }

    @GetMapping("settle_history")
    public String settleHistory(Model model, @LoginUser SessionUser user) {
        if (user == null)
            return "index";
        List<SettleNeedPaidGroupByUserResponseDto> settles = null;
        try {
            settles = settleService.getSettleNeedPaidGroupByUser();
        } catch (Exception e) {
            log.error("failed to read date from settle : " + e.getMessage());
        }
        model.addAttribute("user", user);
        model.addAttribute("settledUsers", settles);
        return "admin/settle_history";
    }
}
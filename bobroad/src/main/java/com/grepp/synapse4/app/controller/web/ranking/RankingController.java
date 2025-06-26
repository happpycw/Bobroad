package com.grepp.synapse4.app.controller.web.ranking;

import com.grepp.synapse4.app.model.restaurant.RestaurantService;
import com.grepp.synapse4.app.model.user.BookmarkService;
import com.grepp.synapse4.app.model.user.CustomUserDetailsService;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import com.grepp.synapse4.app.model.user.dto.RankingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingController {


    private final RestaurantService restaurantService;
    private final BookmarkService bookmarkService;

    @GetMapping
    public String ranking(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<RankingDto> ranking = restaurantService.getRestaurantRanking();
        model.addAttribute("userId",userDetails == null ? 0 : userDetails.getUser().getId());
        model.addAttribute("ranking", ranking);
        return "ranking/ranking";
    }
}

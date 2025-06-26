package com.grepp.synapse4.app.controller.web.restaurant;

import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.entity.RestaurantMenu;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantRepository;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;

    @Value("${kakao.maps.api-key}")
    private String kakaoMapApiKey;

    @GetMapping("restaurant/detail/{id}")
    public String restaurantDetail(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        // exception 처리(CurationController와 공통 사항 noList)
        Restaurant restaurant = restaurantRepository.findWithMenusById(id)
                .orElseThrow(null);

        model.addAttribute("restaurant", restaurant); // null 가능성 있음

        if (restaurant != null && restaurant.getMenus() != null) {
            List<RestaurantMenu> topMenus = restaurant.getMenus().stream()
                    .sorted((a, b) -> b.getPrice() - a.getPrice())
                    .limit(3)
                    .toList();
            model.addAttribute("topMenus", topMenus);
        }
        model.addAttribute("userId",userDetails == null ? 0 : userDetails.getUser().getId());
        model.addAttribute("kakaoMapApiKey", kakaoMapApiKey);
        return "restaurant/detail";
    }
}

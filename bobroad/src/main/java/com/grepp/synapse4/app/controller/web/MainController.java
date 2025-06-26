package com.grepp.synapse4.app.controller.web;

import com.grepp.synapse4.app.model.restaurant.RestaurantService;
import com.grepp.synapse4.app.model.restaurant.dto.RestaurantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final RestaurantService restaurantService;

    @Value("${kakao.maps.api-key}")
    private String kakaoMapApiKey;

    @GetMapping("/")
    public String main(Model model) {
        List<RestaurantDto> allRestaurants = restaurantService.getAllRestaurants();
        model.addAttribute("kakaoMapApiKey", kakaoMapApiKey);
        model.addAttribute("restaurants", allRestaurants);
        return "main";
    }
    
}

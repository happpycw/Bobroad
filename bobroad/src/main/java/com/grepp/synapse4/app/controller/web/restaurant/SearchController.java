package com.grepp.synapse4.app.controller.web.restaurant;

import com.grepp.synapse4.app.model.restaurant.RestaurantSearchService;
import com.grepp.synapse4.app.model.restaurant.dto.search.SearchRestaurantResponseDto;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final RestaurantSearchService restaurantSearchService;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/search")
    public String mainSearch() {
        return "restaurant/search";
    }


    @GetMapping("/search/result")
    public String searchResult(@RequestParam(required = false) String restaurantKeyword, Model model) {
        if(restaurantKeyword == null || restaurantKeyword.isBlank()) {
            model.addAttribute("message", "검색어를 입력해 주세요");
            return "search-result";
        }

            List<SearchRestaurantResponseDto> results = restaurantSearchService.searchByName(restaurantKeyword);
            model.addAttribute("keyword", restaurantKeyword);
            model.addAttribute("results", results);

        return "restaurant/search-result";
    }

}

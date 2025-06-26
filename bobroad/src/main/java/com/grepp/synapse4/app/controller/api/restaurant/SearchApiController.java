package com.grepp.synapse4.app.controller.api.restaurant;

import com.grepp.synapse4.app.model.restaurant.RestaurantSearchService;
import com.grepp.synapse4.app.model.restaurant.dto.search.SearchRestaurantRequestDto;
import com.grepp.synapse4.app.model.restaurant.dto.search.SearchRestaurantResponseDto;
import com.grepp.synapse4.infra.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class SearchApiController {

    private final RestaurantSearchService restaurantSearchService;

    @GetMapping("/api/search")
    public ResponseEntity<?> searchRestaurants(@Valid @ModelAttribute SearchRestaurantRequestDto requestDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(msg); // -> "검색어는 필수입니다."
        }

        List<SearchRestaurantResponseDto> respon = restaurantSearchService.searchByName(requestDto.getRestaurantKeyword());
        return ResponseEntity.ok(respon);
    }

    @GetMapping("/api/restaurants/search")
    public ResponseEntity<ApiResponse<List<SearchRestaurantResponseDto>>> search(
        @RequestParam("keyword") String restaurantKeyword
    ){
        List<SearchRestaurantResponseDto> results = restaurantSearchService.searchByName(restaurantKeyword);

        return ResponseEntity.ok(ApiResponse.success(results));
    }
}

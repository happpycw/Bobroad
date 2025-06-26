package com.grepp.synapse4.app.controller.web;

import com.grepp.synapse4.app.model.restaurant.repository.KakaoRestaurantVerifierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class VerifyTestController {

    private final KakaoRestaurantVerifierService kakaoRestaurantVerifierService;

    // 경로 앞에 /admin 추가?
    @GetMapping("/verify")
    private String verifyRestaurant() {
        kakaoRestaurantVerifierService.verifyAndActivateRestaurant();
        return "카카오 검증 완료";
    }
}

package com.grepp.synapse4.app.controller.web.recommend;

import com.grepp.synapse4.app.model.llm.CurationService;
import com.grepp.synapse4.app.model.llm.dto.UserCurationDto;
import com.grepp.synapse4.app.model.llm.dto.UserCurationSurveyDto;
import com.grepp.synapse4.app.model.llm.dto.UserCurationSurveyDto.RestaurantDto;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CurationController {

    private final CurationService curationService;

    @GetMapping("/curation")
    public String curationList(Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {

        UserCurationSurveyDto surveys;

        if (userDetails != null) {
            // 로그인 유저: SurveyDto 1개
            Long userId = ((CustomUserDetails) userDetails).getUser().getId();
            surveys = curationService.recommendCurationSurveys(userId);

        } else {
            // 비로그인 유저: Optional<UserCurationDto> → UserCurationSurveyDto
            surveys = curationService.getLatestCurationRestaurants()
                    .filter(dto -> dto.getRestaurants() != null && !dto.getRestaurants().isEmpty())
                    .map(dto -> {
                        List<RestaurantDto> restaurants = dto.getRestaurants().stream()
                                .map(cr -> new RestaurantDto(
                                        cr.getId(),
                                        cr.getName(),
                                        cr.getCategory(),
                                        cr.getRoadAddress(),
                                        cr.getBranch(),
                                        cr.getReason(),
                                        cr.getBusinessTime()
                                ))
                                .toList();

                        return new UserCurationSurveyDto(
                                dto.getId(),
                                dto.getTitle(),
                                restaurants
                        );
                    })
                    .orElse(new UserCurationSurveyDto(null, "큐레이션이 없습니다.", List.of()));
        }

        model.addAttribute("curation", surveys);
        return "recommend/user-curation";
    }
}


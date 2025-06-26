package com.grepp.synapse4.app.controller.web.user;

import com.grepp.synapse4.app.model.user.SurveyService;
import com.grepp.synapse4.app.model.user.code.Companion;
import com.grepp.synapse4.app.model.user.code.CompanyLocation;
import com.grepp.synapse4.app.model.user.code.FavoriteCategory;
import com.grepp.synapse4.app.model.user.code.PreferredMood;
import com.grepp.synapse4.app.model.user.code.Purpose;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import com.grepp.synapse4.app.model.user.dto.request.SurveyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @GetMapping
    public String showSurveyForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        if (userDetails.getUser().getIsSurvey()) {
            return "user/redirect-popup";
        }

        model.addAttribute("surveyRequest", new SurveyRequest());
        addEnumAttributes(model);

        return "user/survey-form";
    }

    @PostMapping
    public String submitSurvey(@ModelAttribute SurveyRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        surveyService.submitSurvey(request, userDetails.getUser());
        return "redirect:/";
    }

    private void addEnumAttributes(Model model) {
        model.addAttribute("companylocation", CompanyLocation.values());
        model.addAttribute("purpose", Purpose.values());
        model.addAttribute("companion", Companion.values());
        model.addAttribute("favoritecategory", FavoriteCategory.values());
        model.addAttribute("preferredmood", PreferredMood.values());
    }
}

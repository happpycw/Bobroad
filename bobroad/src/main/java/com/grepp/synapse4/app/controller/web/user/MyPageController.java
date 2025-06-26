package com.grepp.synapse4.app.controller.web.user;

import com.grepp.synapse4.app.model.user.SurveyService;
import com.grepp.synapse4.app.model.user.UserService;
import com.grepp.synapse4.app.model.user.code.Companion;
import com.grepp.synapse4.app.model.user.code.CompanyLocation;
import com.grepp.synapse4.app.model.user.code.FavoriteCategory;
import com.grepp.synapse4.app.model.user.code.PreferredMood;
import com.grepp.synapse4.app.model.user.code.Purpose;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import com.grepp.synapse4.app.model.user.dto.request.EditInfoRequest;
import com.grepp.synapse4.app.model.user.dto.request.SurveyRequest;
import com.grepp.synapse4.app.model.user.entity.User;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final UserService userService;
    private final SurveyService surveyService;

    @GetMapping
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/user/signin";
        }

        User user = userDetails.getUser();

        model.addAttribute("user", user);
        return "user/mypage";
    }

    @GetMapping("/edit-info")
    public String editInfoForm(@AuthenticationPrincipal CustomUserDetails userDetails,
        Model model) {
        User user = userDetails.getUser();
        EditInfoRequest dto = new EditInfoRequest();
        dto.setUserAccount(user.getUserAccount());
        dto.setNickname(user.getNickname());
        dto.setEmail(user.getEmail());
        model.addAttribute("editRequest", dto);
        return "user/edit-info";
    }

    @PostMapping("/edit-info")
    public String updateInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @ModelAttribute("editRequest") EditInfoRequest request,
        BindingResult bindingResult) {

        User user = userDetails.getUser();
        userService.validateEditUser(request, user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "user/edit-info";
        }

        userService.updateUserInfo(request, user);
        return "redirect:/mypage";
    }

    @GetMapping("/edit-prefer")
    public String showEditForm(@AuthenticationPrincipal CustomUserDetails userDetails,
        Model model) {

        Long userId = userDetails.getUser().getId();

        Optional<SurveyRequest> optionalSurvey = surveyService.findSurveyRequest(userId);

        if (optionalSurvey.isPresent()) {
            model.addAttribute("surveyRequest", optionalSurvey.get());
            model.addAttribute("hasSurvey", true);

            Long surveyId = surveyService.getSurveyId(userDetails.getUser());
            model.addAttribute("surveyId", surveyId);

        } else {
            model.addAttribute("surveyRequest", new SurveyRequest());
            model.addAttribute("hasSurvey", false);
        }

        addEnumsAttributes(model);
        return "user/edit-survey";
    }

    @PostMapping("/edit-prefer")
    public String updateSurvey(@ModelAttribute SurveyRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();
        Long surveyId = surveyService.getSurveyId(userDetails.getUser());

        surveyService.updateSurvey(surveyId, request, userDetails.getUser());
        return "redirect:/mypage/edit-prefer?success";
    }

    private void addEnumsAttributes(Model model) {
        model.addAttribute("companylocation", CompanyLocation.values());
        model.addAttribute("purpose", Purpose.values());
        model.addAttribute("companion", Companion.values());
        model.addAttribute("favoritecategory", FavoriteCategory.values());
        model.addAttribute("preferredmood", PreferredMood.values());
    }
}
package com.grepp.synapse4.app.controller.web.user;

import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginRedirectController {
    @GetMapping("/login-redirect")
    public String redirectAfterLogin(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails.getUser().getIsSurvey() ? "redirect:/" : "redirect:/surveys";
    }
}

package com.grepp.synapse4.app.controller.web.oauth2;

import com.grepp.synapse4.app.model.auth.AuthService;
import com.grepp.synapse4.app.model.auth.code.Provider;
import com.grepp.synapse4.app.model.auth.code.Role;
import com.grepp.synapse4.app.model.auth.token.dto.TokenDto;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import com.grepp.synapse4.infra.auth.token.TokenCookieFactory;
import com.grepp.synapse4.infra.auth.token.code.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final UserRepository userRepository;
    private final AuthService authService;

    @GetMapping("/nickname")
    public String showNicknamePage() {
        return "oauth2/nickname";
    }

    @PostMapping("/nickname")
    public String signupFromOAuth2(@RequestParam("nickname") String nickname,
        HttpServletRequest request,
        HttpServletResponse response) {

        String email = (String) request.getSession().getAttribute("oauth2_email");
        String providerId = (String) request.getSession().getAttribute("oauth2_providerId");
        String providerName = (String) request.getSession().getAttribute("oauth2_provider");
        String name = (String) request.getSession().getAttribute("oauth2_name");

        Provider provider = Provider.valueOf(providerName);

        if (userRepository.existsByNickname(nickname)) {
            return "redirect:/oauth2/nickname?error=duplicated";
        }

        User user = User.builder()
            .userAccount(provider.name().toLowerCase() + "_" + providerId)
            .password(null)
            .email(email)
            .name(name)
            .nickname(nickname)
            .isSurvey(false)
            .activated(true)
            .role(Role.ROLE_USER)
            .provider(provider)
            .providerId(providerId)
            .build();

        userRepository.save(user);

        // JWT 발급 및 쿠키 저장
        TokenDto tokenDto = authService.processTokenSignin(user);

        ResponseCookie accessTokenCookie = TokenCookieFactory.create(
            TokenType.ACCESS_TOKEN.name(),
            tokenDto.getAccessToken(),
            tokenDto.getAtExpiresIn());
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
            TokenType.REFRESH_TOKEN.name(),
            tokenDto.getRefreshToken(),
            tokenDto.getRtExpiresIn());

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        // 설문조사 여부에 따라 리다이렉트 경로 분기
        boolean done = Boolean.TRUE.equals(user.getIsSurvey());
        String targetUrl = done ? "/" : "/surveys";

        return "redirect:" + targetUrl;
    }
}


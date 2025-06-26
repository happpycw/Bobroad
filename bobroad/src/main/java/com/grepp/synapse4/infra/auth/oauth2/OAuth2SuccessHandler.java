package com.grepp.synapse4.infra.auth.oauth2;

import com.grepp.synapse4.app.model.auth.AuthService;
import com.grepp.synapse4.app.model.auth.code.Provider;
import com.grepp.synapse4.app.model.auth.token.dto.TokenDto;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import com.grepp.synapse4.infra.auth.oauth2.user.OAuth2UserInfo;
import com.grepp.synapse4.infra.auth.oauth2.user.OAuth2UserInfoFactory;
import com.grepp.synapse4.infra.auth.token.TokenCookieFactory;
import com.grepp.synapse4.infra.auth.token.code.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String     registrationId = "google";              // 현재 구글만 사용
        OAuth2UserInfo userInfo   = OAuth2UserInfoFactory.create(registrationId, oAuth2User);

        // 1. 이미 로컬 가입된 이메일이면 소셜 로그인 차단
        Optional<User> emailUser = userRepository.findByEmail(userInfo.getEmail());
        if (emailUser.isPresent() && emailUser.get().getProvider() == Provider.LOCAL) {
            response.sendRedirect("/user/signin?error=already_local");
            return;
        }

        // 기존 소셜 유저이면 바로 JWT 발급 → 쿠키로 내려주고 홈으로
        Optional<User> socialUser =
            userRepository.findByProviderAndProviderId(userInfo.getProvider(), userInfo.getProviderId());

        if (socialUser.isPresent()) {
            User user = socialUser.get();

            TokenDto dto = authService.processTokenSignin(user);

            ResponseCookie accessCookie  = TokenCookieFactory.create(
                TokenType.ACCESS_TOKEN.name(),
                dto.getAccessToken(),
                dto.getAtExpiresIn());

            ResponseCookie refreshCookie = TokenCookieFactory.create(
                TokenType.REFRESH_TOKEN.name(),
                dto.getRefreshToken(),
                dto.getRtExpiresIn());

            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());

            // 설문조사 여부에 따라 리다이렉트 경로 분기
//            String targetUrl = Boolean.TRUE.equals(user.getIsSurvey())
//                ? "/"
//                : "/surveys";
            response.sendRedirect("/");
            return;
        }

        // 3. 신규 소셜 유저 -> 닉네임 설정 페이지
        request.getSession().setAttribute("oauth2_email",      userInfo.getEmail());
        request.getSession().setAttribute("oauth2_providerId", userInfo.getProviderId());
        request.getSession().setAttribute("oauth2_provider",   userInfo.getProvider().name());
        request.getSession().setAttribute("oauth2_name",       userInfo.getName());
        response.sendRedirect("/oauth2/nickname");
    }
}
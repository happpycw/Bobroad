package com.grepp.synapse4.infra.auth.token.filter;

import com.grepp.synapse4.app.model.auth.token.RefreshTokenService;
import com.grepp.synapse4.app.model.auth.token.UserBlackListRepository;
import com.grepp.synapse4.app.model.auth.token.entity.UserBlackList;
import com.grepp.synapse4.infra.auth.token.JwtProvider;
import com.grepp.synapse4.infra.auth.token.TokenCookieFactory;
import com.grepp.synapse4.infra.auth.token.code.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserBlackListRepository userBlackListRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 로그아웃 요청이 아닌 경우 필터 통과
        if (!path.equals("/api/auth/logout") || !method.equalsIgnoreCase("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);

        if (accessToken != null) {
            try {
                Claims claims = jwtProvider.parseClaim(accessToken);
                // 블랙리스트 등록
                userBlackListRepository.save(new UserBlackList(claims.getSubject()));
                // 리프레시 토큰 삭제
                refreshTokenService.deleteByAccessTokenId(claims.getId());
            } catch (Exception e) {
                // 예외 무시하고 쿠키 삭제 및 리다이렉트 처리
            }
        }

        // 만료된 쿠키로 덮어씌움 (로그아웃 처리)
        response.addHeader("Set-Cookie", TokenCookieFactory.createExpiredToken(TokenType.ACCESS_TOKEN).toString());
        response.addHeader("Set-Cookie", TokenCookieFactory.createExpiredToken(TokenType.REFRESH_TOKEN).toString());
        response.addHeader("Set-Cookie", TokenCookieFactory.createExpiredToken(TokenType.AUTH_SERVER_SESSION_ID).toString());

        response.sendRedirect("/");
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//        FilterChain filterChain) throws ServletException, IOException {
//
//        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);
//
//        if(accessToken == null){
//            filterChain.doFilter(request,response);
//            return;
//        }
//
//        String path = request.getRequestURI();
//        Claims claims  = jwtProvider.parseClaim(accessToken);
//
//        if(path.equals("/api/auth/logout")){
//
//            refreshTokenService.deleteByAccessTokenId(claims.getId());
//            ResponseCookie expiredAccessToken = TokenCookieFactory.createExpiredToken(TokenType.ACCESS_TOKEN);
//            ResponseCookie expiredRefreshToken = TokenCookieFactory.createExpiredToken(TokenType.REFRESH_TOKEN);
//            ResponseCookie expiredSessionId = TokenCookieFactory.createExpiredToken(TokenType.AUTH_SERVER_SESSION_ID);
//
//            response.addHeader("Set-Cookie", expiredAccessToken.toString());
//            response.addHeader("Set-Cookie", expiredRefreshToken.toString());
//            response.addHeader("Set-Cookie", expiredSessionId.toString());
//
//            response.sendRedirect("/");
//            return;
//        }
//
//        filterChain.doFilter(request,response);
//    }
}

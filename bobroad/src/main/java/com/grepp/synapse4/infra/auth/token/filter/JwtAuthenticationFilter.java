package com.grepp.synapse4.infra.auth.token.filter;

import com.grepp.synapse4.app.model.auth.token.RefreshTokenService;
import com.grepp.synapse4.app.model.auth.token.UserBlackListRepository;
import com.grepp.synapse4.app.model.auth.token.dto.AccessTokenDto;
import com.grepp.synapse4.app.model.auth.token.entity.RefreshToken;
import com.grepp.synapse4.app.model.auth.token.entity.UserBlackList;
import com.grepp.synapse4.infra.auth.token.JwtProvider;
import com.grepp.synapse4.infra.auth.token.TokenCookieFactory;
import com.grepp.synapse4.infra.auth.token.code.TokenType;
import com.grepp.synapse4.infra.error.exceptions.CommonException;
import com.grepp.synapse4.infra.response.ResponseCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserBlackListRepository userBlackListRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<String> excludePath = new ArrayList<>();
        excludePath.addAll(List.of("/error", "/favicon.ico", "/css", "/img","/js","/download"));
        excludePath.addAll(List.of("/user/signup", "/user/signin"));
        String path = request.getRequestURI();
        return excludePath.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        log.info(request.getRequestURI());

        String requestAccessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);
        log.info("📌 AccessToken in cookie: {}", requestAccessToken);
        if (requestAccessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtProvider.parseClaim(requestAccessToken);
        if(userBlackListRepository.existsById(claims.getSubject())){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if(jwtProvider.validateToken(requestAccessToken)){
                Authentication authentication = jwtProvider.generateAuthentication(requestAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException ex) {
            AccessTokenDto newAccessToken = renewingAccessToken(requestAccessToken, request);
            if (newAccessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            RefreshToken newRefreshToken = renewingRefreshToken(claims.getId(), newAccessToken.getId());
            responseToken(response, newAccessToken, newRefreshToken);
        }

        filterChain.doFilter(request, response);
    }

    private void responseToken(HttpServletResponse response, AccessTokenDto newAccessToken,
        RefreshToken newRefreshToken) {

        ResponseCookie accessTokenCookie =
            TokenCookieFactory.create(TokenType.ACCESS_TOKEN.name(), newAccessToken.getToken(),
                3000000L);

        ResponseCookie refreshTokenCookie =
            TokenCookieFactory.create(TokenType.REFRESH_TOKEN.name(), newRefreshToken.getToken(),
                jwtProvider.getAtExpiration());

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    private RefreshToken renewingRefreshToken(String id, String newTokenId) {
        return refreshTokenService.renewingToken(id, newTokenId);
    }

    private AccessTokenDto renewingAccessToken(String requestAccessToken, HttpServletRequest request) {
        Authentication authentication = jwtProvider.generateAuthentication(requestAccessToken);
        String refreshToken = jwtProvider.resolveToken(request, TokenType.REFRESH_TOKEN);
        Claims claims = jwtProvider.parseClaim(requestAccessToken);

        RefreshToken storedRefreshToken = refreshTokenService.findByAccessTokenId(claims.getId());

        if(storedRefreshToken == null) {
            return null;
        }

        if (!storedRefreshToken.getToken().equals(refreshToken)) {
            userBlackListRepository.save(new UserBlackList(authentication.getName()));
            throw new CommonException(ResponseCode.SECURITY_INCIDENT);
        }

        return generateAccessToken(authentication);
    }

    private AccessTokenDto generateAccessToken(Authentication authentication) {
        AccessTokenDto newAccessToken = jwtProvider.generateAccessToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return newAccessToken;
    }
}

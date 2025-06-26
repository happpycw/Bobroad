package com.grepp.synapse4.app.model.auth;

import com.grepp.synapse4.app.model.auth.token.RefreshTokenRepository;
import com.grepp.synapse4.app.model.auth.token.UserBlackListRepository;
import com.grepp.synapse4.app.model.auth.token.dto.AccessTokenDto;
import com.grepp.synapse4.app.model.auth.token.dto.TokenDto;
import com.grepp.synapse4.app.model.auth.token.entity.RefreshToken;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import com.grepp.synapse4.infra.auth.token.JwtProvider;
import com.grepp.synapse4.infra.auth.token.code.GrantType;
import com.grepp.synapse4.infra.error.exceptions.LocalUserExistsException;
import com.grepp.synapse4.infra.error.exceptions.NeedNicknameRegistrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserBlackListRepository userBlackListRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public TokenDto signin(String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            username, password);
        // loadUserByUsername + password 검증 후 authentication 반환
        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 사용자 정보 조회
        User user = userRepository.findByUserAccount(username)
            .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        return processTokenSignin(user);
    }

    public TokenDto processTokenSignin(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NeedNicknameRegistrationException(email));

        if (user.getProvider().name().equalsIgnoreCase("LOCAL")) {
            throw new LocalUserExistsException();
        }

        return processTokenSignin(user);
    }

    public TokenDto processTokenSignin(User user) {
        // 블랙리스트에서 제거
        userBlackListRepository.deleteById(user.getUserAccount());

        AccessTokenDto dto = jwtProvider.generateAccessToken(user.getUserAccount());
        RefreshToken refreshToken = new RefreshToken(user.getUserAccount(), dto.getId());
        refreshTokenRepository.save(refreshToken);

        return TokenDto.builder()
            .accessToken(dto.getToken())
            .refreshToken(refreshToken.getToken())
            .atExpiresIn(jwtProvider.getAtExpiration())
            .rtExpiresIn(jwtProvider.getRtExpiration())
            .grantType(GrantType.BEARER)
            .user(user)
            .build();
    }
}


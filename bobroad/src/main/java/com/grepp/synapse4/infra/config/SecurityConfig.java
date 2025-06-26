package com.grepp.synapse4.infra.config;

import static org.springframework.http.HttpMethod.POST;

import com.grepp.synapse4.infra.auth.oauth2.OAuth2FailureHandler;
import com.grepp.synapse4.infra.auth.oauth2.OAuth2SuccessHandler;
import com.grepp.synapse4.infra.auth.token.JwtAuthenticationEntryPoint;
import com.grepp.synapse4.infra.auth.token.filter.AuthExceptionFilter;
import com.grepp.synapse4.infra.auth.token.filter.JwtAuthenticationFilter;
import com.grepp.synapse4.infra.auth.token.filter.LogoutFilter;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthExceptionFilter authExceptionFilter;
    private final JwtAuthenticationEntryPoint entryPoint;
    private final LogoutFilter logoutFilter;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF, Form Login, HTTP Basic 인증 비활성화
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            // 세션을 사용하지 않으므로 STATELESS로 설정
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // oauth 2.0 login
            .oauth2Login(oauth -> oauth
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
            )

            .authorizeHttpRequests(auth -> auth
                // 관리자(admin)
                .requestMatchers(
                    "/admin/signin", "/admin/signup", "/img/**", "/css/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 공개
                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/static/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(POST, "/api/auth/logout").permitAll()
                .requestMatchers("/user/**", "/search/**", "/curation/**",
                    "/restaurant/**", "/ranking/**").permitAll()

                // 인증 필요한 사용자 경로
                .requestMatchers(
                    "api/bookmarks/toggle/**", "/api/users/**", "/api/user/**",
                    "/recommend/**", "/meetings/**", "/edit-prefer/**",
                    "/mypage/**", "/bookmarks/**", "/surveys/**"
                ).authenticated()

                .anyRequest().permitAll()
            )
            // JWT 인증 실패 시 처리할 EntryPoint 설정
            .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(authExceptionFilter, JwtAuthenticationFilter.class)
            .addFilterBefore(logoutFilter, AuthExceptionFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
        throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(Collections.singletonList(
            "http://localhost:8080"
        ));

        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST"));
        corsConfig.setAllowedHeaders(Collections.singletonList("*"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}



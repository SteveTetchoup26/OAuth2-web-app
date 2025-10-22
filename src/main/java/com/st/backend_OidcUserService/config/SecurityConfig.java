package com.st.backend_OidcUserService.config;

import com.st.backend_OidcUserService.security.CustomOAuth2UserService;
import com.st.backend_OidcUserService.security.CustomOidcUserService;
import com.st.backend_OidcUserService.security.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final OAuth2LoginSuccessHandler successHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          CustomOidcUserService customOidcUserService,
                          OAuth2LoginSuccessHandler successHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOidcUserService = customOidcUserService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "style.css",
                                "script.js",
                                "assets/**",
                                "/h2-console/**",
                                "/auth/**",
                                "/oauth2/**",
                                "/login/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService)
                        )
                        .successHandler(successHandler)
                );

        // H2 console frames
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
package com.st.backend_OidcUserService.security;

import com.st.backend_OidcUserService.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final String frontendRedirect = "http://localhost:8080/index.html";

    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = (String) oauthUser.getAttributes().get("email");

        // Génère un JWT backend (notre token) pour frontend
        String token = jwtUtil.generateToken(email);

        // Option A: redirection + token en query param (simple, mais token visible dans URL)
         response.sendRedirect(frontendRedirect + "?token=" + token);

        // Option B: set cookie httpOnly
//        response.addHeader("Set-Cookie", "AUTH_TOKEN=" + token + "; HttpOnly; Path=/; Max-Age=3600");
//        response.sendRedirect(frontendRedirect + "/");
    }
}

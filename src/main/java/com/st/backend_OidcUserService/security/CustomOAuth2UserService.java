package com.st.backend_OidcUserService.security;


import com.st.backend_OidcUserService.service.UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attrs = oauth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = (String) attrs.get("sub");
        String email = (String) attrs.get("email");
        String firstName = (String) attrs.get("given_name");
        String lastName = (String) attrs.get("family_name");

        userService.saveOrUpdateOAuth2User(provider, providerId, email, firstName, lastName);

        return oauth2User;
    }
}

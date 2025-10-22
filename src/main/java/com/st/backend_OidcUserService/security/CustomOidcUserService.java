package com.st.backend_OidcUserService.security;

import com.st.backend_OidcUserService.service.UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserService userService;

    public CustomOidcUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {

        // Charger l'utilisateur OAuth2/OIDC
        OidcUser oidcUser = super.loadUser(userRequest);
        Map<String, Object> attrs = oidcUser.getAttributes();

        // Extraire les informations
        String provider = userRequest.getClientRegistration().getRegistrationId(); // "google"
        String providerId = oidcUser.getSubject(); // ou (String) attrs.get("sub")
        String email = oidcUser.getEmail();
        String firstName = oidcUser.getGivenName();
        String lastName = oidcUser.getFamilyName();


        // Sauvegarder ou mettre à jour l'utilisateur
        userService.saveOrUpdateOAuth2User(
                provider,
                providerId,
                email,
                firstName,
                lastName
        );

        return oidcUser;
    }
}
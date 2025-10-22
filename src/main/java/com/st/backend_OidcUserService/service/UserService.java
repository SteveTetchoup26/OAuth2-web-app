package com.st.backend_OidcUserService.service;

import com.st.backend_OidcUserService.model.User;
import com.st.backend_OidcUserService.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User saveOrUpdateOAuth2User(String provider, String providerId, String email, String firstName, String lastName) {
        Optional<User> existingOpt = userRepository.findByProviderAndProviderId(provider, providerId)
                .or(() -> userRepository.findByEmail(email));

        User user;
        if (existingOpt.isPresent()) {
            user = existingOpt.get();
        } else {
            user = new User();
        }

        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setProvider(provider);
        user.setProviderId(providerId);

        User saved = userRepository.save(user);
        return saved;
    }
}


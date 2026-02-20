package com.kivora.JsWater.infrastructure.security.auth;

import com.kivora.JsWater.domain.model.user.User;
import com.kivora.JsWater.domain.model.user.UserStatus;
import com.kivora.JsWater.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException(username));

       return org.springframework.security.core.userdetails.User
               .withUsername(user.getName())
               .password(user.getPassword())
               .roles(user.getRole().name())
               .disabled(user.getStatus() != UserStatus.ACTIVE)
               .build();
    }
}

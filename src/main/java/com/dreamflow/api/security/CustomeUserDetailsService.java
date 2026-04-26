package com.dreamflow.api.security;

import com.dreamflow.api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomeUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    @Cacheable(value="user", key="#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.dreamflow.api.auth.entity.User user = userRepository.findByEmail(username).orElseThrow(()->new RuntimeException("User Not Found"));

        UserDetails userDetails = new CustomUserDetails(user.getUserId(), user.getEmail(), user.getPassword());

        return userDetails;
    }
}

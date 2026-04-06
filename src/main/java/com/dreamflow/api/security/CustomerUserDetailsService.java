package com.dreamflow.api.security;

import com.dreamflow.api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.dreamflow.api.auth.entity.User user = userRepository.findByEmail(username).orElseThrow(()->new RuntimeException("User Not Found"));

        return User.builder().username(user.getEmail()).password(user.getPassword()).build();
    }
}

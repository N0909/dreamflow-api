package com.dreamflow.api.admin.users.service;

import com.dreamflow.api.admin.users.dto.AdminUserDTO;
import com.dreamflow.api.admin.users.dto.AdminUserDetails;
import com.dreamflow.api.admin.users.dto.CreateAdminRequest;
import com.dreamflow.api.admin.users.specification.SearchType;
import com.dreamflow.api.admin.users.specification.UserSpecification;
import com.dreamflow.api.auth.entity.Role;
import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.auth.repository.UserRepository;
import com.dreamflow.api.exception.exceptions.ResourceAlreadyExistException;
import com.dreamflow.api.exception.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AdminUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<AdminUserDTO> getUsers(
            Role role,
            String email,
            String username,
            LocalDate createdAfter,
            LocalDate createdBefore,
            LocalDate createdAt,
            SearchType searchType,
            Pageable pageable
    ) {

        Specification<User> specification =
                UserSpecification.filterUsers(
                        role,
                        email,
                        username,
                        createdAfter,
                        createdBefore,
                        createdAt,
                        searchType
                );

        Page<AdminUserDTO> users = userRepository
                .findAll(specification, pageable)
                .map(user -> new AdminUserDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                ));

        return users;
    }

    public AdminUserDetails getUser(int userId){
        User user = userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User Doesn't exist")
        );

        return new AdminUserDetails(user.getUserId(), user.getUsername(), user.getEmail(), user.getRole(), user.getCreatedAt(), user.getSongs().size());
    }
    @Transactional
    public AdminUserDTO updateRole(int userId, Role userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id " + userId + " doesn't exist"
                        ));

        user.setRole(userRole);

        userRepository.save(user);

        return new AdminUserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}

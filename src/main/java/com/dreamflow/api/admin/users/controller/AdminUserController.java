package com.dreamflow.api.admin.users.controller;

import com.dreamflow.api.admin.users.dto.AdminUserDTO;
import com.dreamflow.api.admin.users.dto.AdminUserDetails;
import com.dreamflow.api.admin.users.dto.CreateAdminRequest;
import com.dreamflow.api.admin.users.service.AdminUserService;
import com.dreamflow.api.admin.users.specification.SearchType;
import com.dreamflow.api.auth.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService){
        this.adminUserService = adminUserService;
    }

    @GetMapping()
    public ResponseEntity<Page<AdminUserDTO>> getUsers(
            @RequestParam(value="user-role", required = false) Role role,
            @RequestParam(value="email", required = false) String email,
            @RequestParam(value="username", required = false) String username,
            @RequestParam(value="created-after", required = false)LocalDate createdAfter,
            @RequestParam(value="created-before", required = false) LocalDate createdBefore,
            @RequestParam(value="created-at", required = false) LocalDate createdAt,
            @RequestParam(value="search-type", defaultValue = "CONTAINS")SearchType searchType,
            @RequestParam(value="page-no", defaultValue = "0") int pageNo,
            @RequestParam(value="page-size", defaultValue = "10")int pageSize
    ){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<AdminUserDTO> users = adminUserService.getUsers(
                role,
                email,
                username,
                createdAfter,
                createdBefore,
                createdAt,
                searchType,
                pageable
        );

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(users);
    }
    @GetMapping("{id}")
    public ResponseEntity<AdminUserDetails> getUser(@PathVariable("id") int userId){
        AdminUserDetails user = adminUserService.getUser(userId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(user);
    }
    @PatchMapping("/update-role/{id}/{ROLE}")
    public ResponseEntity<AdminUserDTO> createAdmin(@PathVariable("id") int userId, @PathVariable("ROLE") Role role){
        AdminUserDTO admin = adminUserService.updateRole(userId, role);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(admin);
    }
}

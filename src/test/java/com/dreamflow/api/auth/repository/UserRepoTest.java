package com.dreamflow.api.auth.repository;

import com.dreamflow.api.auth.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class UserRepoTest {
//    @Autowired
    private UserRepository userRepository;

//    @Test
    public void testUser(){
        User user = userRepository.findByEmail("johndoe9@gmail.com").orElseThrow(()->new RuntimeException(""));
        System.out.println(user);
    }
}

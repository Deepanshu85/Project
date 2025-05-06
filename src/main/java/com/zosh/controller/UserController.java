package com.zosh.controller;

import com.zosh.domain.USER_ROLE;
import com.zosh.modal.User;
import com.zosh.response.AuthResponse;
import com.zosh.response.SignupRequest;
import com.zosh.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/profile")
    public ResponseEntity<User> createUserHandler
            (@RequestHeader("Authorization") String jwt) throws Exception {
//-----------myown
        if (jwt.startsWith("Bearer")) {
            jwt = jwt.substring(7);
        }
        // Debugging log
        System.out.println("Processed JWT: " + jwt);

        User user = userService.findUserByJwtToken(jwt);

        return ResponseEntity.ok(user);
    }

}

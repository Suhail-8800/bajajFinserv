// package com.example.webhooksqlsolver.controller;

// import java.util.Map;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import com.example.webhooksqlsolver.model.LoginRequest;
// import com.example.webhooksqlsolver.model.UserRegistrationRequest;
// import com.example.webhooksqlsolver.service.UserService;

// @RestController
// @RequestMapping("/api/users")
// public class UserController {

//     private final UserService userService;

//     public UserController(UserService userService) {
//         this.userService = userService;
//     }

//     @PostMapping("/login")
//     public ResponseEntity<?> loginUser(
//             @RequestBody LoginRequest request) {

//         String token = userService.login(
//                 request.getEmail(),
//                 request.getPassword()
//         );

//         return ResponseEntity.ok(
//                 Map.of(
//                         "message", "Login successful",
//                         "token", token
//                 )
//         );
//     }


//     @PostMapping("/register")
//     public ResponseEntity<?> registerUser(
//             @RequestBody UserRegistrationRequest request) {

//         var user = userService.createUser(
//                 request.getName(),
//                 request.getEmail(),
//                 request.getPassword(),
//                 "USER"
//         );

//         return ResponseEntity.ok(
//                 Map.of(
//                         "message", "User registered successfully",
//                         "userId", user.getId(),
//                         "email", user.getEmail()
//                 )
//         );
//     }
// }



package com.example.webhooksqlsolver.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webhooksqlsolver.model.LoginRequest;
import com.example.webhooksqlsolver.model.UserRegistrationRequest;
import com.example.webhooksqlsolver.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody LoginRequest request) {

        String token = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message", "Login successful",
                        "token", token
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody UserRegistrationRequest request) {

        var user = userService.createUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                "USER"
        );

        return ResponseEntity.ok(
                Map.of(
                        "message", "User registered successfully",
                        "userId", user.getId(),
                        "email", user.getEmail()
                )
        );
    }
}
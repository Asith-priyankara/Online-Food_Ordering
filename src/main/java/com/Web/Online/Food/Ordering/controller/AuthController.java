package com.Web.Online.Food.Ordering.controller;

import com.Web.Online.Food.Ordering.model.User;
import com.Web.Online.Food.Ordering.request.LoginRequest;
import com.Web.Online.Food.Ordering.response.AuthResponse;
import com.Web.Online.Food.Ordering.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController( AuthService authService) {
        this.authService = authService;

    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody User user) throws Exception {

        AuthResponse authResponse = authService.signUp(user);

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {

        AuthResponse authResponse =  authService.login(loginRequest);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}

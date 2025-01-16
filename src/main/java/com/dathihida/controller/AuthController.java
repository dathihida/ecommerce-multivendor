package com.dathihida.controller;

import com.dathihida.domain.USER_ROLE;
import com.dathihida.model.User;
import com.dathihida.repository.UserRepository;
import com.dathihida.request.SignupRequest;
import com.dathihida.response.AuthResponse;
import com.dathihida.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody SignupRequest request) {
        String jwt = authService.createUser(request);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("register success");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(response);
    }
}

package com.dathihida.controller;

import com.dathihida.domain.USER_ROLE;
import com.dathihida.model.User;
import com.dathihida.model.VerificationCode;
import com.dathihida.repository.UserRepository;
import com.dathihida.request.LoginOtpRequest;
import com.dathihida.request.LoginRequest;
import com.dathihida.request.SignupRequest;
import com.dathihida.response.ApiResponse;
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
    public ResponseEntity<AuthResponse> createUser(@RequestBody SignupRequest request) throws Exception {
        String jwt = authService.createUser(request);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("register success");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtpRequest request) throws Exception {

        authService.sendLoginOpt(request.getEmail(), request.getRole());

        ApiResponse response = new ApiResponse();
        response.setMessage("otp sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) throws Exception {
        String otp = request.getOtp();
        String email = request.getEmail();

        request.setEmail("customer_" + email);
        AuthResponse authResponse = authService.signIn(request);
        return ResponseEntity.ok(authResponse);
    }
}

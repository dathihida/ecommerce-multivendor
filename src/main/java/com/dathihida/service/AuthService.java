package com.dathihida.service;

import com.dathihida.domain.USER_ROLE;
import com.dathihida.request.LoginRequest;
import com.dathihida.request.SignupRequest;
import com.dathihida.response.AuthResponse;

public interface AuthService {
    void sendLoginOpt(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest request) throws Exception;
    AuthResponse signIn(LoginRequest request);
}

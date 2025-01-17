package com.dathihida.service;

import com.dathihida.request.LoginRequest;
import com.dathihida.request.SignupRequest;
import com.dathihida.response.AuthResponse;

public interface AuthService {
    void sendLoginOpt(String email) throws Exception;
    String createUser(SignupRequest request) throws Exception;
    AuthResponse signIn(LoginRequest request);
}

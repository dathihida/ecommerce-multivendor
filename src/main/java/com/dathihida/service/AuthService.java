package com.dathihida.service;

import com.dathihida.request.SignupRequest;

public interface AuthService {
    void sendLoginOpt(String email) throws Exception;
    String createUser(SignupRequest request) throws Exception;
}

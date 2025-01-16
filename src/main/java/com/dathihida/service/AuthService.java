package com.dathihida.service;

import com.dathihida.request.SignupRequest;

public interface AuthService {
    String createUser(SignupRequest request);
}

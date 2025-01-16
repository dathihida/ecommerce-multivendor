package com.dathihida.request;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String fullName;
    private String opt;
}

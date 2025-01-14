package com.dathihida.domain;

public enum AccountStatus {
    PENDING_VERIFICATION, //Account is created but yet verified
    ACTIVE, // Account is active and in good standing
    SUSPENDED,// Account is tempor
    DEACTIVATED,
    BANNED,
    CLOSED
}

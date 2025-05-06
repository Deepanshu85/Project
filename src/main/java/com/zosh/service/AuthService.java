package com.zosh.service;

import com.zosh.domain.USER_ROLE;
import com.zosh.request.LoginRequest;
import com.zosh.response.AuthResponse;
import com.zosh.response.SignupRequest;

public interface AuthService {
    void sentLoginOtp(String emil, USER_ROLE role) throws Exception;
    String createUser(SignupRequest req) throws Exception;

    AuthResponse signing(LoginRequest req) throws Exception;



}

package com.reservation.item.service;

import com.reservation.item.model.AuthenticationResponse;
import com.reservation.item.model.LoginRequest;
import com.reservation.item.model.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {


     AuthenticationResponse register(RegisterRequest request);

     AuthenticationResponse authenticate(LoginRequest request);
}

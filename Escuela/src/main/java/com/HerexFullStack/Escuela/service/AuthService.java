package com.HerexFullStack.Escuela.service;

import com.HerexFullStack.Escuela.dto.AuthLoginRequestDTO;
import com.HerexFullStack.Escuela.dto.AuthLoginResponseDTO;
import com.HerexFullStack.Escuela.security.config.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public AuthLoginResponseDTO loginUser(AuthLoginRequestDTO request){

        String username = request.username();
        String password = request.password();

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthLoginResponseDTO(
                request.username(),
                "User logged in successfully",
                accessToken,
                true
        );



    }



}

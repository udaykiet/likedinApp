package com.ups.user_service.controllers;

import com.ups.user_service.dtos.LoginRequestDto;
import com.ups.user_service.dtos.SignupRequestDto;
import com.ups.user_service.dtos.UserDto;
import com.ups.user_service.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto){
        UserDto user = authService.signup(signupRequestDto);
        return new ResponseEntity<>(user , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto){
        String accessToken = authService.login(loginRequestDto);
        return ResponseEntity.ok(accessToken);

    }

}

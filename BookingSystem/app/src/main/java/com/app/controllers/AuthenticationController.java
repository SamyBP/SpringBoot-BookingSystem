package com.app.controllers;

import com.app.dtos.SignInRequestDto;
import com.app.dtos.SignInResponseDto;
import com.app.dtos.SignUpRequestDto;
import com.app.models.User;
import com.app.services.AuthenticationService;
import com.app.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "${allowed.origins}")
@RestController
@RequestMapping("api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.ok(authenticationService.register(signUpRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponseDto> login(@RequestBody SignInRequestDto signInRequestDto) {
        User user = authenticationService.authenticate(signInRequestDto);
        String token = jwtService.generateToken(user.getUsername(), user.getId());
        SignInResponseDto signInResponseDto = new SignInResponseDto(true, token);
        return ResponseEntity.ok(signInResponseDto);
    }
}

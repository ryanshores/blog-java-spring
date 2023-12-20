package com.ryanshores.blog.controllers;

import com.ryanshores.blog.payloads.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping
    ResponseEntity<Authentication> getAuthentication() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(auth, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

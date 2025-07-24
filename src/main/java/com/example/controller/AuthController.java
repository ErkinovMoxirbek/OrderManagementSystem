package com.example.controller;

import com.example.dto.AuthRequestDTO;
import com.example.dto.AuthResponseDTO;
import com.example.dto.create.ProfileCreateDTO;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/registration")
    public ResponseEntity<String> create(@Valid @RequestBody ProfileCreateDTO dto) {
        return ResponseEntity.ok(authService.registration(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authorization(@Valid @RequestBody AuthRequestDTO dto) {
        AuthResponseDTO result = authService.authorization(dto);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/registration/email/verification/{token}")
    public ResponseEntity<String> registration(@PathVariable("token") String token) {
        return ResponseEntity.ok(authService.regEmailVerification(token));
    }
}

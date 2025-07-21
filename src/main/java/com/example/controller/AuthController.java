package com.example.controller;

import com.example.dto.AuthRequestDTO;
import com.example.dto.AuthResponseDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.create.ProfileCreateDTO;
import com.example.service.AuthService;
import com.example.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/auth")
public class AuthController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private AuthService authService;
    @PostMapping("/registration")
    public ResponseEntity<ProfileDTO> create(@RequestBody ProfileCreateDTO dto) {
        ProfileDTO result = authService.registration(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authorization(@RequestBody AuthRequestDTO dto) {
        AuthResponseDTO result = authService.authorization(dto);
        return ResponseEntity.ok(result);
    }
}

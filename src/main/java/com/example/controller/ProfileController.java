package com.example.controller;

import com.example.dto.AuthRequestDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.update.ProfileChangePasswordDTO;
import com.example.dto.update.ProfileUpdateDTO;
import com.example.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/profiles")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/{email}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable String email) {
        return ResponseEntity.ok(profileService.getByEmail(email));
    }
    @GetMapping
    public ResponseEntity<List<ProfileDTO>> getAll() {
        return ResponseEntity.ok(profileService.getAll());
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Boolean> deleteProfile(@PathVariable String email) {
        return ResponseEntity.ok(profileService.deleteProfile(email));
    }
    @PutMapping
    public ResponseEntity<ProfileUpdateDTO> updateProfile(@RequestBody ProfileUpdateDTO profileUpdateDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(profileService.updateProfile(email, profileUpdateDTO));
    }
    @PutMapping("/change-password")
    public ResponseEntity<String> updatePassword(@RequestBody AuthRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(profileService.updatePassword(email,dto));
    }
    @PutMapping("/change-password/{token}")
    public ResponseEntity<String> changePassword(@PathVariable String token, @RequestBody ProfileChangePasswordDTO dto) {
        return ResponseEntity.ok(profileService.changePassword(token, dto.getPassword()));
    }

}
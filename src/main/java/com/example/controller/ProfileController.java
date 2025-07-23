package com.example.controller;

import com.example.dto.AuthRequestDTO;
import com.example.dto.AuthResponseDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.create.ProductCreateDTO;
import com.example.dto.create.ProfileCreateDTO;
import com.example.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileCreateDTO profileCreateDTO) {
        return ResponseEntity.ok(profileService.creatProfile(profileCreateDTO));
    }
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
}
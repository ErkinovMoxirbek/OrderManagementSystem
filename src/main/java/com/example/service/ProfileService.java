package com.example.service;

import com.example.config.CustomUserDetails;
import com.example.dto.AuthRequestDTO;
import com.example.dto.AuthResponseDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.repository.ProfileRepository;
import com.example.util.JwtUtil;
import com.example.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    //Log
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);
    public ProfileDTO registration(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if (optional.isPresent()) {
            logger.error("Profile null");
            return null;
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setRole(dto.getRole());

        profileRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }


    public AuthResponseDTO authorization(AuthRequestDTO auth) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()));

            if (authentication.isAuthenticated()) {
                CustomUserDetails profile = (CustomUserDetails) authentication.getPrincipal();
                AuthResponseDTO response = new AuthResponseDTO();
                response.setName(profile.getName());
                response.setSurname(profile.getSurname());
                response.setEmail(profile.getEmail());
                response.setRole(profile.getRole());
                response.setJwtToken(JwtUtil.encode(profile.getEmail(), profile.getRole()));
                return response;
            }
        } catch (BadCredentialsException e) {
            throw new UsernameNotFoundException("Phone or password wrong");
        }
        throw new UsernameNotFoundException("Phone or password wrong");
    }
}
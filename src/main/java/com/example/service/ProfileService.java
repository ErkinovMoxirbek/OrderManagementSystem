package com.example.service;

import com.example.config.CustomUserDetails;
import com.example.dto.AuthRequestDTO;
import com.example.dto.AuthResponseDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.create.ProductCreateDTO;
import com.example.dto.create.ProfileCreateDTO;
import com.example.entity.ProfileEntity;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.exception.OrderAlreadyExistsException;
import com.example.repository.ProfileRepository;
import com.example.util.JwtUtil;
import com.example.util.MD5Util;
import org.apache.commons.validator.routines.EmailValidator;
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

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    //Log
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);
    public ProfileDTO registration(ProfileCreateDTO profileCreateDTO) {
        ProfileDTO dto = new ProfileDTO();
        dto.setFullName(profileCreateDTO.getFullName());
        dto.setEmail(profileCreateDTO.getEmail());
        dto.setPassword(passwordEncoder.encode(profileCreateDTO.getPassword()));
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if (optional.isPresent()) {
            logger.warn("Profile is already exists {}", dto.getEmail());
            throw new OrderAlreadyExistsException(dto.getEmail());
        }
        if (dto.getEmail() == null || !isValidEmail(dto.getEmail())) {
            logger.warn("Email is not valid {}", dto.getEmail());
            throw new BadRequestException("Email format is incorrect: " + dto.getEmail());
        }
        if (dto.getFullName() == null || dto.getFullName().length() < 3 ) {
            logger.warn("Name is too short {}", dto.getFullName());
            throw new BadRequestException("The customer's name is not valid: " + dto.getFullName());
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 3 ) {
            logger.warn("Password is too short min len:3 -> your pass: {}", dto.getPassword());
            throw new BadRequestException("The customer's password is not valid min len:3 -> your pass: " + dto.getPassword());
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        profileRepository.save(entity);
        dto.setId(entity.getId());
        dto.setRole(entity.getRole());
        return dto;
    }


    public AuthResponseDTO authorization(AuthRequestDTO auth) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()));

            if (authentication.isAuthenticated()) {
                CustomUserDetails profile = (CustomUserDetails) authentication.getPrincipal();
                AuthResponseDTO response = new AuthResponseDTO();
                response.setFullName(profile.getFullName());
                response.setEmail(profile.getEmail());
                response.setRole(profile.getRole());
                response.setJwtToken(JwtUtil.encode(profile.getEmail(), profile.getRole()));
                return response;
            }
        } catch (BadCredentialsException e) {
            throw new UsernameNotFoundException("Email or password wrong");
        }
        throw new UsernameNotFoundException("Email or password wrong");
    }
    public ProfileDTO getByEmail(String email) {
        if (!isValidEmail(email)) {
            logger.warn("Email is not valid {}", email);
            throw new BadRequestException("Email format is incorrect.: " + email);
        }
        ProfileDTO profileDTO = profileRepository.findByEmailDTO(email);
        if (profileDTO == null) {
            logger.warn("Profile not found {}", email);
            throw new NotFoundException("Profile not found: " + email);
        }
        return profileDTO;
    }
    public boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public List<ProfileDTO> getAll() {
        return profileRepository.findAllDTO()   ;
    }
}
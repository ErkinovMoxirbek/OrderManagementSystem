package com.example.service;

import com.example.dto.AuthRequestDTO;
import com.example.dto.AuthResponseDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.create.ProfileCreateDTO;
import com.example.dto.update.ProfileUpdateDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.GeneralStatus;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.repository.ProfileRepository;
import com.example.service.email.EmailSenderService;
import com.example.util.JwtUtil;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    //Log
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);
    @Autowired
    private AuthService authService;
    @Autowired
    private EmailSenderService emailSenderService;

    public ProfileDTO getByEmail(String email) {
        if (isValidEmail(email)) {
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
        return !EmailValidator.getInstance().isValid(email);
    }

    public List<ProfileDTO> getAll() {
        return profileRepository.findAllDTO()   ;
    }

    public Boolean deleteProfile(String email) {
        if (isValidEmail(email)) {
            logger.warn("Email is not valid {}", email);
            throw new BadRequestException("Email format is incorrect.: " + email);
        }
        ProfileEntity profileEntity = profileRepository.findByEmailAndVisibleTrue(email).get();
        profileEntity.setVisible(!profileEntity.getVisible());
        profileRepository.save(profileEntity);
        return true;
    }

    public ProfileUpdateDTO updateProfile(String email, ProfileUpdateDTO dto) {
        ProfileEntity entity = profileRepository.findByEmailAndVisibleTrue(email)
                .orElseThrow(() -> new NotFoundException("Profile not found"));

        if (dto.getFullName() != null) entity.setFullName(dto.getFullName());

        profileRepository.save(entity);

        return toDTO(entity);
    }

    private ProfileUpdateDTO toDTO(ProfileEntity entity) {
        ProfileUpdateDTO dto = new ProfileUpdateDTO();
        dto.setEmail(entity.getEmail());
        dto.setFullName(entity.getFullName());
        return dto;
    }

    public String updatePassword(String email, AuthRequestDTO dto) {
        AuthResponseDTO dto1 = authService.authorization(dto);
        if (dto1 == null) {
            logger.warn("Profile not found {}", email);
            throw new NotFoundException("Profile not found : " + email);
        }
        emailSenderService.sendPasswordResetEmail(dto1.getEmail());
        logger.info("sending password reset email");
        return "Sending password reset email";
    }

    public String changePassword(String token, String password) {
        String email;
        try {
            JwtDTO jwtDTO = JwtUtil.decodeRegistrationToken(token);
            email = jwtDTO.getEmail();
        } catch (Exception e) {
            throw new RuntimeException("Token noto‘g‘ri yoki eskirgan!");
        }
        ProfileEntity profile = profileRepository.findByEmailAndVisibleTrue(email)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));
        profile.setPassword(passwordEncoder.encode(password));
        profileRepository.save(profile);
        return "Profile password changed";
    }
}
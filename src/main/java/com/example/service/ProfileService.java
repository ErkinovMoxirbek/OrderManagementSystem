package com.example.service;

import com.example.dto.AuthResponseDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.create.ProfileCreateDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.GeneralStatus;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.repository.ProfileRepository;
import org.apache.commons.lang3.builder.Diff;
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

    public ProfileDTO creatProfile(ProfileCreateDTO profileCreateDTO) {
        if (profileCreateDTO == null){
            throw new NotFoundException("profile is null");
        }
        if (!EmailValidator.getInstance().isValid(profileCreateDTO.getEmail())) {
            throw new BadRequestException("Invalid email address");
        }
        if (profileCreateDTO.getPassword() == null || profileCreateDTO.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }
        if (profileCreateDTO.getFullName() == null || profileCreateDTO.getFullName().length() < 3) {
            throw new BadRequestException("Full name must be at least 3 characters");
        }
        // Agar profile mavjud bolsa mavjud profileni update qilish
        if (profileRepository.findByEmail(profileCreateDTO.getEmail()).isPresent() && !profileRepository.findByEmail(profileCreateDTO.getEmail()).get().getVisible()) {
            ProfileEntity profileEntity = profileRepository.findByEmail(profileCreateDTO.getEmail()).get();
            profileEntity.setPassword(passwordEncoder.encode(profileCreateDTO.getPassword()));
            profileEntity.setFullName(profileCreateDTO.getFullName());
            profileEntity.setVisible(true);
            profileEntity.setStatus(GeneralStatus.ACTIVE);
            profileRepository.save(profileEntity);
            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setId(profileEntity.getId());
            profileDTO.setEmail(profileCreateDTO.getEmail());
            profileDTO.setFullName(profileCreateDTO.getFullName());
            profileDTO.setPassword(passwordEncoder.encode(profileCreateDTO.getPassword()));
            profileDTO.setRole(profileEntity.getRole());
            return profileDTO;
        }
        //Profile create
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(profileCreateDTO.getEmail());
        profileEntity.setFullName(profileCreateDTO.getFullName());
        profileEntity.setPassword(profileCreateDTO.getPassword());
        profileRepository.save(profileEntity);
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profileEntity.getId());
        profileDTO.setEmail(profileCreateDTO.getEmail());
        profileDTO.setFullName(profileCreateDTO.getFullName());
        profileDTO.setPassword(passwordEncoder.encode(profileCreateDTO.getPassword()));
        profileDTO.setRole(profileEntity.getRole());
        return profileDTO;
    }
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
        ProfileEntity profileEntity = profileRepository.findByEmail(email).get();
        profileEntity.setVisible(!profileEntity.getVisible());
        profileRepository.save(profileEntity);
        return true;
    }
}
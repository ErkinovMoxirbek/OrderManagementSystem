package com.example.service;

import com.example.dto.AuthRequestDTO;
import com.example.dto.AuthResponseDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.create.ProfileCreateDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.GeneralStatus;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.exception.OrderAlreadyExistsException;
import com.example.repository.ProfileRepository;
import com.example.service.email.EmailHistoryService;
import com.example.service.email.EmailSenderService;
import com.example.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Autowired
    private EmailSenderService emailSenderService;

    //Log
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

    public String registration(ProfileCreateDTO profileCreateDTO) {
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
        if (dto.getFullName() == null || dto.getFullName().length() < 3) {
            logger.warn("Name is too short {}", dto.getFullName());
            throw new BadRequestException("The customer's name is not valid: " + dto.getFullName());
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 3) {
            logger.warn("Password is too short min len:3 -> your pass: {}", dto.getPassword());
            throw new BadRequestException("The customer's password is not valid min len:3 -> your pass: " + dto.getPassword());
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        profileRepository.save(entity);
        emailSenderService.sendRegistrationStyledEmail(profileCreateDTO.getEmail());// profile emailiga verfi link jonab ketadi!
        return "Verifikatsiya kodi " + entity.getEmail() + " manziliga yuborildi. Iltimos, pochtangizni tekshiring!";

    }


    public AuthResponseDTO authorization(AuthRequestDTO auth) {
        isValidEmail(auth.getEmail());
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndVisibleTrue(auth.getEmail());
        if (optional.isEmpty()) {
            throw new NotFoundException("Profile Not found");
        }
        ProfileEntity profile = optional.get();
        if (!passwordEncoder.matches(auth.getPassword(), profile.getPassword())) {
            throw new NotFoundException("Password Wrong");
        }
        if (profile.getVerified() == null){
            emailSenderService.sendRegistrationStyledEmail(profile.getEmail());
            throw new BadRequestException("No verified: Email send verified link");
        }
        if (!profile.getVerified() && !profile.getVisible()){
            throw new BadRequestException("Account Not Verified : " + auth.getEmail());
        }
        AuthResponseDTO response = new AuthResponseDTO();
        response.setFullName(profile.getFullName());
        response.setEmail(profile.getEmail());
        response.setRole(profile.getRole());

        response.setJwtToken(JwtUtil.encode(profile.getEmail(), profile.getRole()));
        return response;
    }

    public boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public String regEmailVerification(String token) {
        JwtDTO jwtDTO = null;
        try {
            jwtDTO = JwtUtil.decodeRegistrationToken(token);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException("JWT Expired");
        } catch (JwtException e) {
            throw new BadRequestException("JWT Not Valid");
        }
        String email = jwtDTO.getEmail();

        Optional<ProfileEntity> existOptional = profileRepository.findByEmailAndVisibleTrue(email);
        if (existOptional.isEmpty()) {
            throw new BadRequestException("Email not found");
        }
        ProfileEntity profile = existOptional.get();
        if (!profile.getStatus().equals(GeneralStatus.NOACTIVE)) {
            throw new BadRequestException("Email int wrong status");
        }
        // check fo sms code and time
        if (emailHistoryService.isSmsSendToAccount(email, jwtDTO.getUnique())) {
            profile.setStatus(GeneralStatus.ACTIVE);
            profile.setVerified(true);
            profileRepository.save(profile);
            return "Verification successfully completed";
        }
        throw new BadRequestException("Not completed");
    }
}

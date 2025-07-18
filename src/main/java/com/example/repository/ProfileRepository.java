package com.example.repository;
import com.example.dto.OrderDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {

    Optional<ProfileEntity> findByEmailAndVisibleTrue(String phone);
    @Query("""
        SELECT new com.example.dto.ProfileDTO(
            o.id,
            o.fullName,
            o.email,
            o.password,
            o.role
        )
        FROM ProfileEntity o
        WHERE o.email = :email
    """)
    ProfileDTO findByEmailDTO(String email);
    @Query("""
        SELECT new com.example.dto.ProfileDTO(
            o.id,
            o.fullName,
            o.email,
            o.password,
            o.role
        )
        FROM ProfileEntity o
    """)
    List<ProfileDTO> findAllDTO();

    Optional<ProfileEntity> findByEmail(String email);
}
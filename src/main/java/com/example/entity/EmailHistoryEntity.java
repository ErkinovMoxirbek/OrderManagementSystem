package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "email_history")
public class EmailHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "to_account")
    private String toAccount;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Column(name = "unique_info")
    private String unique;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;
}

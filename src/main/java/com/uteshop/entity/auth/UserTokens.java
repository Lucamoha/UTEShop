package com.uteshop.entity.auth;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTokens implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    Users user;

    @Column(nullable = false)
    int Type; // 1=verify_email, 2=reset_password

    @Column(nullable = false, unique = true, length = 100)
    String Token;

    @Column(nullable = false)
    LocalDateTime ExpiredAt;

    LocalDateTime UsedAt;

    @Column(nullable = false)
    LocalDateTime CreatedAt;
}

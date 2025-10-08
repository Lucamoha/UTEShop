package com.uteshop.entity.branch;

import com.uteshop.entity.auth.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Branches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(200)")
    String Name;

    @Column(columnDefinition = "NVARCHAR(300)")
    String Address;

    @Column(length = 20)
    String Phone;

    @Column(nullable = false)
    Boolean IsActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManagerId")
    Users manager;

    @Column(nullable = false)
    LocalDateTime CreatedAt;

    LocalDateTime UpdatedAt;
    
    @PrePersist
	void onCreate() {
		//Tự động gán giá trị khi insert mới
		CreatedAt = LocalDateTime.now();
		UpdatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	void onUpdate() {
		// Tự động cập nhật UpdatedAt khi update
		UpdatedAt = LocalDateTime.now();
	}
}

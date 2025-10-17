package com.uteshop.entity.auth;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.uteshop.entity.cart.Carts;
import com.uteshop.entity.engagement.Reviews;
import com.uteshop.entity.order.Orders;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer Id;

	@Column(nullable = false, unique = true)
	String Email;

	@Column(nullable = false, columnDefinition = "VARCHAR(MAX)")
	String PasswordHash;

	@Column(name = "FullName", nullable = false, columnDefinition = "NVARCHAR(120)")
	String FullName;
	
	String Phone;

	@Column(nullable = false, length = 20)
	String UserRole; // USER|MANAGER|ADMIN

	@Column(nullable = false)
	Boolean isActive = true;

	@Column(nullable = false)
	LocalDateTime CreatedAt;

	@Column(nullable = false)
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
	
	// Quan hệ
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    //cascade = CascadeType.ALL -> ap dung cac cascade: merge, persist, remove, refresh, detached
    //orphanRemoval = true -> xoa cha thi xoa con
    private List<Addresses> addresses = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<UserTokens> tokens = new ArrayList<>();

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	Carts cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Reviews> reviews = new ArrayList<>();

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}

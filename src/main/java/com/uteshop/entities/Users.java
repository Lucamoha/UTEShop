package com.uteshop.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	Integer Id;
	
	String Email;

	String PasswordHash;
	
	String FullName;
	
	String Phone;
	
	String UserRole;
	
	LocalDateTime CreatedAt;
	
	LocalDateTime UpdatedAt;
	
	// Quan hệ
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    //cascade = CascadeType.ALL -> ap dung cac cascade: merge, persist, remove, refresh, detached
    //orphanRemoval = true -> xoa cha thi xoa con
    private List<Addresses> addresses = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<UserTokens> tokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Carts> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Reviews> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<CompareLists> compareLists = new ArrayList<>();

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPasswordHash() {
		return PasswordHash;
	}

	public void setPasswordHash(String passwordHash) {
		PasswordHash = passwordHash;
	}

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getUserRole() {
		return UserRole;
	}

	public void setUserRole(String userRole) {
		UserRole = userRole;
	}

	public LocalDateTime getCreatedAt() {
		return CreatedAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		CreatedAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return UpdatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		UpdatedAt = updatedAt;
	}

	public List<Addresses> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Addresses> addresses) {
		this.addresses = addresses;
	}

	public List<UserTokens> getTokens() {
		return tokens;
	}

	public void setTokens(List<UserTokens> tokens) {
		this.tokens = tokens;
	}

	public List<Carts> getCarts() {
		return carts;
	}

	public void setCarts(List<Carts> carts) {
		this.carts = carts;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}

	public List<Reviews> getReviews() {
		return reviews;
	}

	public void setReviews(List<Reviews> reviews) {
		this.reviews = reviews;
	}

	public List<CompareLists> getCompareLists() {
		return compareLists;
	}

	public void setCompareLists(List<CompareLists> compareLists) {
		this.compareLists = compareLists;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Users(Integer id, String email, String passwordHash, String fullName, String phone, String userRole,
			LocalDateTime createdAt, LocalDateTime updatedAt, List<Addresses> addresses, List<UserTokens> tokens,
			List<Carts> carts, List<Orders> orders, List<Reviews> reviews, List<CompareLists> compareLists) {
		super();
		Id = id;
		Email = email;
		PasswordHash = passwordHash;
		FullName = fullName;
		Phone = phone;
		UserRole = userRole;
		CreatedAt = createdAt;
		UpdatedAt = updatedAt;
		this.addresses = addresses;
		this.tokens = tokens;
		this.carts = carts;
		this.orders = orders;
		this.reviews = reviews;
		this.compareLists = compareLists;
	}

	public Users() {
		super();
	}
    
    
}

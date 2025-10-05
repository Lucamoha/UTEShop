package com.uteshop.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity

@FieldDefaults(level = AccessLevel.PRIVATE)
@NamedQuery(name = "Products.findAll", query = "select p from Products p")
public class Products implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    Integer Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CategoryId", nullable = false)
    Categories category;

    String Name;

    String Slug;

    String Description;

    BigDecimal BasePrice;

    boolean Status;

    LocalDateTime CreatedAt;
    
    LocalDateTime UpdatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ProductVariants> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ProductImages> images = new ArrayList<>();

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Categories getCategory() {
		return category;
	}

	public void setCategory(Categories category) {
		this.category = category;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getSlug() {
		return Slug;
	}

	public void setSlug(String slug) {
		Slug = slug;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public BigDecimal getBasePrice() {
		return BasePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		BasePrice = basePrice;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
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

	public List<ProductVariants> getVariants() {
		return variants;
	}

	public void setVariants(List<ProductVariants> variants) {
		this.variants = variants;
	}

	public List<ProductImages> getImages() {
		return images;
	}

	public void setImages(List<ProductImages> images) {
		this.images = images;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Products(int id, Categories category, String name, String slug, String description, BigDecimal basePrice,
			boolean status, LocalDateTime createdAt, LocalDateTime updatedAt, List<ProductVariants> variants,
			List<ProductImages> images) {
		super();
		Id = id;
		this.category = category;
		Name = name;
		Slug = slug;
		Description = description;
		BasePrice = basePrice;
		Status = status;
		CreatedAt = createdAt;
		UpdatedAt = updatedAt;
		this.variants = variants;
		this.images = images;
	}

	public Products() {
		super();
	}
    
}

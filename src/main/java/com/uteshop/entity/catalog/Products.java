package com.uteshop.entity.catalog;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"category", "images", "variants"})
@NamedQueries({
        @NamedQuery(name = "Products.findAll", query = "select p from Products p"),
        @NamedQuery(
                name = "Products.findLatestProducts",
                query = "SELECT DISTINCT p FROM Products p " +
                        "ORDER BY p.CreatedAt DESC"
        ),
        @NamedQuery(name = "Products.findByCategoryId",
        query = "SELECT DISTINCT p FROM Products p " +
                "LEFT JOIN FETCH p.images pi " +
                "WHERE p.category.id = :catId AND p.Status = true " +
                "ORDER BY p.CreatedAt DESC"),

        @NamedQuery(name = "Products.countByCategoryId",
        query = "SELECT COUNT(p) FROM Products p " +
                "WHERE p.category.id = :catId AND p.Status = true"
        	),
        @NamedQuery(
                name = "Products.findByCategoryIds",
                query = "SELECT DISTINCT p FROM Products p " +
                        "LEFT JOIN FETCH p.images pi " +
                        "WHERE p.category.id IN :catIds AND p.Status = true " +
                        "ORDER BY p.CreatedAt DESC"
        ),
        @NamedQuery(
                name = "Products.countByCategoryIds",
                query = "SELECT COUNT(p) FROM Products p " +
                        "WHERE p.category.id IN :catIds AND p.Status = true"
        )
})
public class Products implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CategoryId", nullable = false)
    Categories category;

	@Column(nullable = false, columnDefinition = "NVARCHAR(200)")
    String Name;

	@Column(nullable = false, unique = true)
    String Slug;

	@Column(columnDefinition = "NVARCHAR(MAX)")
    String Description;

	@Column(nullable = false, precision = 12, scale = 2)
    BigDecimal BasePrice;

	@Column(nullable = false)
    boolean Status;

	@Column(nullable = false, updatable = false)
    LocalDateTime CreatedAt;
    
	@Column
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
    
    Integer Sold = 0;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ProductVariants> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<ProductImages> images = new ArrayList<>();
}

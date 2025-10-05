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
                        "LEFT JOIN FETCH p.images pi " +
                        "WHERE pi.SortOrder = 0 " +
                        "ORDER BY p.CreatedAt DESC"
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

	@Column(nullable = false)
    LocalDateTime CreatedAt;
    
    LocalDateTime UpdatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ProductVariants> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<ProductImages> images = new ArrayList<>();
}

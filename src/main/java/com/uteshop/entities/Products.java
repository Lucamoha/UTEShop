package com.uteshop.entities;

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
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NamedQuery(name = "Products.findAll", query = "select p from Products p")
public class Products implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    int Id;

    @ManyToOne(fetch = FetchType.LAZY)
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
}

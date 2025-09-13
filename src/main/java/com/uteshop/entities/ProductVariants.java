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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariants implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    int Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductId", nullable = false)
    Products product;

    String SKU;

    BigDecimal Price;

    Integer StockQty;

    Integer Status;

    LocalDateTime CreatedAt;

    LocalDateTime UpdatedAt;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<VariantOptions> options = new ArrayList<>();
}

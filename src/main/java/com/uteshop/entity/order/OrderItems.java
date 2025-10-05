package com.uteshop.entity.order;

import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.Products;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "OrderItems")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItems implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderId", nullable = false)
    Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductId", nullable = false)
    Products product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VariantId")
    ProductVariants variant;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal Price;

    @Column(nullable = false)
    int Quantity;
}

package com.uteshop.entity.cart;

import java.io.Serializable;

import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.Products;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItems implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CartId", nullable = false)
    Carts cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductId", nullable = false)
    Products product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VariantId")
    ProductVariants variant;

    @Column(nullable = false)
    int Quantity;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

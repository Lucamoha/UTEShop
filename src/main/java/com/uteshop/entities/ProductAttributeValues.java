package com.uteshop.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAttributeValues implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    ProductAttributeId id;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductId", nullable = false)
    Products product;

    @MapsId("attributeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AttributeId", nullable = false)
    Attributes attribute;

    String ValueText;

    BigDecimal ValueNumber;
}

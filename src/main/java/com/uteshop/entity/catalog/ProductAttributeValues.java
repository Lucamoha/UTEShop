package com.uteshop.entity.catalog;

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
    Id id;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductId", nullable = false)
    Products product;

    @MapsId("attributeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AttributeId", nullable = false)
    Attributes attribute;

    @Column(columnDefinition = "NVARCHAR(255)")
    String ValueText;

    @Column(precision = 18, scale = 4)
    BigDecimal ValueNumber;

    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "ProductId")
        private Integer productId;

        @Column(name = "AttributeId")
        private Integer attributeId;
    }
}

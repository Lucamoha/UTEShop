package com.uteshop.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryAttributes implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    CategoryAttributeId id;

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId", nullable = false)
    Categories category;

    @MapsId("attributeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AttributeId", nullable = false)
    Attributes attribute;
}

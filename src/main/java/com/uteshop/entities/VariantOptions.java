package com.uteshop.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UQ_VO_Variant_Value", columnNames = {"VariantId", "OptionValueId"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantOptions implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    VariantOptionId id;

    @MapsId("variantId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VariantId", nullable = false)
    ProductVariants variant;

    @MapsId("optionTypeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OptionTypeId", nullable = false)
    OptionTypes optionType;

    @MapsId("optionValueId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OptionValueId", nullable = false)
    OptionValues optionValue;
}

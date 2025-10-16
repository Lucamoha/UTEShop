package com.uteshop.entity.catalog;

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
public class VariantOptions implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    Id id = new Id();

    @MapsId("variantId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VariantId", nullable = false)
    ProductVariants variant;

    @MapsId("optionTypeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OptionTypeId", nullable = false)
    OptionTypes optionType;

    // Không cần field optionValueId nữa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OptionValueId", nullable = false)
    OptionValues optionValue;

    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "VariantId")
        private Integer variantId;

        @Column(name = "OptionTypeId")
        private Integer optionTypeId;
    }
}

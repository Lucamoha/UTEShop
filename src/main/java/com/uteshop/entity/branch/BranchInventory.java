package com.uteshop.entity.branch;

import com.uteshop.entity.catalog.ProductVariants;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchInventory {
    @EmbeddedId
    Id id;

    @MapsId("branchId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BranchId", nullable = false)
    Branches branch;

    @MapsId("variantId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VariantId", nullable = false)
    ProductVariants variant;


    @Column(nullable = false)
    Integer BranchStock = 0;


    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "BranchId")
        private Integer branchId;

        @Column(name = "VariantId")
        private Integer variantId;
    }
}

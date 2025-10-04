package com.uteshop.entity.engagement;

import java.io.Serializable;

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
public class CompareItems implements Serializable {
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    Id id;

    @MapsId("listId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ListId", nullable = false)
    CompareLists list;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductId", nullable = false)
    Products product;

    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "ListId")
        private Integer listId;

        @Column(name = "ProductId")
        private Integer productId;
    }
}

package com.uteshop.entity.engagement;

import java.io.Serializable;

import com.uteshop.entity.auth.Users;
import com.uteshop.entity.catalog.Products;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Favorites implements Serializable {
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    Id Id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    Users user;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductId", nullable = false)
    Products product;

    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "UserId")
        private Integer userId;

        @Column(name = "ProductId")
        private Integer productId;
    }
}

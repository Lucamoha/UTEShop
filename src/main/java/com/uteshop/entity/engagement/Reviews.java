package com.uteshop.entity.engagement;

import com.uteshop.entity.auth.Users;
import com.uteshop.entity.catalog.Products;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NamedQuery(name = "Reviews.findAll", query = "select r from Reviews r")
public class Reviews implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductId", nullable = false)
    private Products product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    Users user;

    @Column(nullable = false)
    int Rating; // 1..5

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String Content;

    @Column(nullable = false)
    Boolean HasMedia;

    @Column(nullable = false)
    Boolean PurchaseVerified;

    @Column(nullable = false)
    boolean Status;

    @Column(nullable = false)
    LocalDateTime CreatedAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ReviewMedia> media = new ArrayList<>();
}

package com.uteshop.entities;

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
    int Id;

    Integer ProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    Users user;

    Integer Rating; // 1..5

    String Content;

    Boolean HasMedia;

    Boolean PurchaseVerified;

    Integer Status;

    LocalDateTime CreatedAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<ReviewMedia> media = new ArrayList<>();
}

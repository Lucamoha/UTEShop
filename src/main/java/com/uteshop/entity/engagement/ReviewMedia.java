package com.uteshop.entity.engagement;

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
public class ReviewMedia implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReviewId", nullable = false)
    Reviews review;

    @Column(nullable = false, length = 255)
    String MediaUrl;

    @Column(nullable = false)
    int MediaType; // 1=image,2=video

    @Column(nullable = false)
    int SortOrder;
}

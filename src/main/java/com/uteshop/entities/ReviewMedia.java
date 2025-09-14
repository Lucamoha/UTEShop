package com.uteshop.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewMedia implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReviewId", nullable = false)
    Reviews review;

    String MediaUrl;

    int MediaType; // 1=image,2=video

    int SortOrder;
}

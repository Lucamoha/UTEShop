package com.uteshop.entities;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantOptionId implements Serializable {
    private static final long serialVersionUID = 1L;
    int variantId;
    int optionTypeId;
    int optionValueId;
}

package com.uteshop.dto.web;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantDto {
    private Integer variantId;
    private String sku;
    private BigDecimal price;
    String status;
}

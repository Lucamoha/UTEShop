package com.uteshop.dto.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterOptionsDto {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}

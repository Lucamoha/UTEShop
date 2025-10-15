package com.uteshop.dto.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterOptionsDto {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    
    // Map<AttributeId, AttributeInfo>
    private Map<Integer, AttributeFilterDto> attributes;
}

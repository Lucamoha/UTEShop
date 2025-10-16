package com.uteshop.dto.web;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO cho kết quả so sánh 2 sản phẩm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductComparisonDto {
    // Thông tin sản phẩm 1
    Integer product1Id;
    String product1Name;
    String product1Slug;
    BigDecimal product1Price;
    String product1Image;
    
    // Thông tin sản phẩm 2
    Integer product2Id;
    String product2Name;
    String product2Slug;
    BigDecimal product2Price;
    String product2Image;
    
    // Thông tin category
    Integer categoryId;
    String categoryName;
    
    // Danh sách các thuộc tính để so sánh
    List<ComparisonAttributeDto> comparableAttributes;
}

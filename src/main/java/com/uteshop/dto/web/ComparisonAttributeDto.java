package com.uteshop.dto.web;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO cho một thuộc tính trong bảng so sánh sản phẩm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComparisonAttributeDto {
    Integer attributeId;
    String attributeName;
    String unit; // Đơn vị đo lường
    int dataType; // 1=text, 2=number, 3=boolean
    String product1Value; // Giá trị của sản phẩm 1
    String product2Value; // Giá trị của sản phẩm 2
}

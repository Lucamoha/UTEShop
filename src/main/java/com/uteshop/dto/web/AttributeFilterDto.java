package com.uteshop.dto.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO chứa thông tin attribute để hiển thị filter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeFilterDto {
    private Integer id;
    private String name;
    private Integer dataType; // 1=TEXT, 2=NUMBER, 3=BOOLEAN
    private String unit;
    
    // Danh sách các giá trị có thể filter (cho TEXT type)
    private List<String> possibleValues;
    
    // Danh sách các giá trị NUMBER có thể filter (cho NUMBER type)
    private List<Double> possibleNumberValues;
    
    // Range cho NUMBER type (deprecated - giữ lại để tương thích)
    private Double minValue;
    private Double maxValue;
}

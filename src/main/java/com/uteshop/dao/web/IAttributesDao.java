package com.uteshop.dao.web;

import com.uteshop.entity.catalog.Attributes;

import java.util.List;
import java.util.Map;

public interface IAttributesDao {
    /**
     * Lấy danh sách attributes theo categoryIds
     * @param categoryIds Danh sách category IDs
     * @return List of Attributes
     */
    List<Attributes> getAttributesByCategoryIds(List<Integer> categoryIds);
    
    /**
     * Lấy các giá trị TEXT có thể có của một attribute trong các products thuộc categoryIds
     * @param attributeId ID của attribute
     * @param categoryIds Danh sách category IDs
     * @return List of distinct text values
     */
    List<String> getPossibleTextValues(Integer attributeId, List<Integer> categoryIds);
    
    /**
     * Lấy range (min, max) của NUMBER attribute trong các products thuộc categoryIds
     * @param attributeId ID của attribute
     * @param categoryIds Danh sách category IDs
     * @return Map với keys "min" và "max"
     */
    Map<String, Double> getNumberRange(Integer attributeId, List<Integer> categoryIds);
    
    /**
     * Lấy các giá trị NUMBER riêng biệt của một attribute trong các products thuộc categoryIds
     * @param attributeId ID của attribute
     * @param categoryIds Danh sách category IDs
     * @return List of distinct number values
     */
    List<Double> getPossibleNumberValues(Integer attributeId, List<Integer> categoryIds);

    /**
     * Lấy danh sách attributes và giá trị của một sản phẩm
     * @param productId ID của sản phẩm
     * @return Map với key là tên attribute, value là giá trị
     */
    Map<String, String> getProductAttributes(int productId);
}

package com.uteshop.dao.impl.web.catalog;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.web.catalog.IProductComparisonDao;
import com.uteshop.dto.web.ComparisonAttributeDto;
import com.uteshop.dto.web.ProductComparisonDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ProductComparisonDaoImpl implements IProductComparisonDao {
    @Override
    public ProductComparisonDto compareProducts(int product1Id, int product2Id) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            // Lấy thông tin cơ bản của 2 sản phẩm
            String productInfoJpql = """
                SELECT p.Id, p.Name, p.Slug, p.BasePrice, 
                       COALESCE(p.category.parent.Id, p.category.Id), 
                       COALESCE(p.category.parent.Name, p.category.Name)
                FROM Products p
                WHERE p.Id IN (:product1Id, :product2Id)
                ORDER BY CASE WHEN p.Id = :product1Id THEN 1 ELSE 2 END
                """;
            
            List<Object[]> productInfos = em.createQuery(productInfoJpql, Object[].class)
                    .setParameter("product1Id", product1Id)
                    .setParameter("product2Id", product2Id)
                    .getResultList();
            
            if (productInfos.size() != 2) {
                return null; // Không tìm thấy đủ 2 sản phẩm
            }
            
            // Parse thông tin sản phẩm 1
            Object[] p1Info = productInfos.get(0);
            Integer p1Id = (Integer) p1Info[0];
            String p1Name = (String) p1Info[1];
            String p1Slug = (String) p1Info[2];
            BigDecimal p1Price = (BigDecimal) p1Info[3];
            Integer categoryId = (Integer) p1Info[4];
            String categoryName = (String) p1Info[5];
            
            // Parse thông tin sản phẩm 2
            Object[] p2Info = productInfos.get(1);
            Integer p2Id = (Integer) p2Info[0];
            String p2Name = (String) p2Info[1];
            String p2Slug = (String) p2Info[2];
            BigDecimal p2Price = (BigDecimal) p2Info[3];
            
            // Lấy ảnh riêng cho từng sản phẩm
            String p1Image = getFirstProductImage(em, p1Id);
            String p2Image = getFirstProductImage(em, p2Id);
            
            // Lấy danh sách các attributes có thể so sánh
            // Attributes được định nghĩa ở parent category (categoryId đã là parent từ COALESCE ở trên)
            String comparableAttrsJpql = """
                SELECT DISTINCT a.Id, a.Name, a.Unit, a.DataType
                FROM CategoryAttributes ca
                JOIN ca.attribute a
                WHERE ca.category.Id = :categoryId
                AND ca.IsComparable = true
                ORDER BY a.Name
                """;
            
            List<Object[]> comparableAttrs = em.createQuery(comparableAttrsJpql, Object[].class)
                    .setParameter("categoryId", categoryId)
                    .getResultList();
            
            // Lấy giá trị của các attributes cho cả 2 sản phẩm
            List<ComparisonAttributeDto> comparisonAttributes = new ArrayList<>();
            
            for (Object[] attr : comparableAttrs) {
                Integer attrId = (Integer) attr[0];
                String attrName = (String) attr[1];
                String unit = (String) attr[2];
                Integer dataType = (Integer) attr[3];
                
                // Lấy giá trị cho sản phẩm 1
                String p1Value = getAttributeValue(em, product1Id, attrId, dataType);
                
                // Lấy giá trị cho sản phẩm 2
                String p2Value = getAttributeValue(em, product2Id, attrId, dataType);
                
                // Thêm vào danh sách so sánh
                ComparisonAttributeDto comparisonAttr = ComparisonAttributeDto.builder()
                        .attributeId(attrId)
                        .attributeName(attrName)
                        .unit(unit)
                        .dataType(dataType)
                        .product1Value(p1Value)
                        .product2Value(p2Value)
                        .build();
                
                comparisonAttributes.add(comparisonAttr);
            }
            
            // Tạo ProductComparisonDto
            return ProductComparisonDto.builder()
                    .product1Id(p1Id)
                    .product1Name(p1Name)
                    .product1Slug(p1Slug)
                    .product1Price(p1Price)
                    .product1Image(p1Image)
                    .product2Id(p2Id)
                    .product2Name(p2Name)
                    .product2Slug(p2Slug)
                    .product2Price(p2Price)
                    .product2Image(p2Image)
                    .categoryId(categoryId)
                    .categoryName(categoryName)
                    .comparableAttributes(comparisonAttributes)
                    .build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
    
    // Lấy giá trị của một attribute cho một sản phẩm
    private String getAttributeValue(EntityManager em, int productId, int attributeId, int dataType) {
        try {
            String jpql = """
                SELECT pav.ValueText, pav.ValueNumber
                FROM ProductAttributeValues pav
                WHERE pav.product.Id = :productId AND pav.attribute.Id = :attributeId
                """;
            
            Object[] result = em.createQuery(jpql, Object[].class)
                    .setParameter("productId", productId)
                    .setParameter("attributeId", attributeId)
                    .getSingleResult();
            
            String valueText = (String) result[0];
            BigDecimal valueNumber = (BigDecimal) result[1];
            
            // Xử lý theo dataType
            if (dataType == 1) { // Text
                return valueText != null ? valueText : "N/A";
            } else if (dataType == 2) { // Number
                return valueNumber != null ? valueNumber.toString() : "N/A";
            } else if (dataType == 3) { // Boolean
                return valueText != null ? valueText : "N/A";
            }
            
            return "N/A";
        } catch (NoResultException e) {
            return "N/A";
        }
    }
    
    // Lấy ảnh đầu tiên của sản phẩm
    private String getFirstProductImage(EntityManager em, int productId) {
        try {
            String jpql = """
                SELECT pi.ImageUrl 
                FROM ProductImages pi 
                WHERE pi.product.Id = :productId 
                ORDER BY pi.Id ASC
                """;
            
            List<String> images = em.createQuery(jpql, String.class)
                    .setParameter("productId", productId)
                    .setMaxResults(1)
                    .getResultList();
            
            return images.isEmpty() ? null : images.get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public ProductComparisonDto compareProductsBySlug(String product1Slug, String product2Slug) {
        // Lấy ID từ slug
        Integer product1Id = getProductIdBySlug(product1Slug);
        Integer product2Id = getProductIdBySlug(product2Slug);
        
        if (product1Id == null || product2Id == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm với slug đã cho");
        }
        
        // Gọi method compareProducts với ID
        return compareProducts(product1Id, product2Id);
    }
    
    @Override
    public Integer getProductIdBySlug(String slug) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = "SELECT p.Id FROM Products p WHERE p.Slug = :slug";
            return em.createQuery(jpql, Integer.class)
                    .setParameter("slug", slug)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}

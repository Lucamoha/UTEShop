package com.uteshop.dao.impl.web;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.web.IAttributesDao;
import com.uteshop.entity.catalog.Attributes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesDaoImpl implements IAttributesDao {

    @Override
    public List<Attributes> getAttributesByCategoryIds(List<Integer> categoryIds) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = """
                SELECT DISTINCT a 
                FROM Attributes a
                JOIN CategoryAttributes ca ON a.Id = ca.attribute.Id
                WHERE ca.category.Id IN :categoryIds
                  AND ca.IsFilterable = true
                ORDER BY a.Name
                """;
            
            TypedQuery<Attributes> query = em.createQuery(jpql, Attributes.class);
            query.setParameter("categoryIds", categoryIds);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<String> getPossibleTextValues(Integer attributeId, List<Integer> categoryIds) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = """
                SELECT DISTINCT pav.ValueText
                FROM ProductAttributeValues pav
                WHERE pav.attribute.Id = :attributeId
                  AND pav.product.category.Id IN :categoryIds
                  AND pav.ValueText IS NOT NULL
                  AND pav.product.Status = true
                ORDER BY pav.ValueText
                """;
            
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            query.setParameter("attributeId", attributeId);
            query.setParameter("categoryIds", categoryIds);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Map<String, Double> getNumberRange(Integer attributeId, List<Integer> categoryIds) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = """
                SELECT MIN(pav.ValueNumber), MAX(pav.ValueNumber)
                FROM ProductAttributeValues pav
                WHERE pav.attribute.Id = :attributeId
                  AND pav.product.category.Id IN :categoryIds
                  AND pav.ValueNumber IS NOT NULL
                  AND pav.product.Status = true
                """;
            
            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setParameter("attributeId", attributeId);
            query.setParameter("categoryIds", categoryIds);
            
            Object[] result = query.getSingleResult();
            Map<String, Double> range = new HashMap<>();
            
            if (result[0] != null && result[1] != null) {
                range.put("min", ((Number) result[0]).doubleValue());
                range.put("max", ((Number) result[1]).doubleValue());
            } else {
                range.put("min", 0.0);
                range.put("max", 0.0);
            }
            
            return range;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Double> getPossibleNumberValues(Integer attributeId, List<Integer> categoryIds) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = """
                SELECT DISTINCT pav.ValueNumber
                FROM ProductAttributeValues pav
                WHERE pav.attribute.Id = :attributeId
                  AND pav.product.category.Id IN :categoryIds
                  AND pav.ValueNumber IS NOT NULL
                  AND pav.product.Status = true
                ORDER BY pav.ValueNumber
                """;
            
            TypedQuery<java.math.BigDecimal> query = em.createQuery(jpql, java.math.BigDecimal.class);
            query.setParameter("attributeId", attributeId);
            query.setParameter("categoryIds", categoryIds);
            
            // Convert BigDecimal to Double
            return query.getResultList().stream()
                    .map(java.math.BigDecimal::doubleValue)
                    .collect(java.util.stream.Collectors.toList());
        } finally {
            em.close();
        }
    }

    @Override
    public Map<String, String> getProductAttributes(int productId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = """
                SELECT a.Name, a.Unit, a.DataType, pav.ValueText, pav.ValueNumber
                FROM ProductAttributeValues pav
                JOIN pav.attribute a
                WHERE pav.product.Id = :productId
                ORDER BY a.Name
                """;

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setParameter("productId", productId);
            List<Object[]> results = query.getResultList();

            Map<String, String> attributes = new HashMap<>();

            for (Object[] row : results) {
                String attrName = (String) row[0];
                String unit = (String) row[1];
                Integer dataType = (Integer) row[2];
                String valueText = (String) row[3];
                java.math.BigDecimal valueNumber = (java.math.BigDecimal) row[4];

                String displayValue;

                // Xử lý theo dataType
                if (dataType == 1) { // Text
                    displayValue = valueText != null ? valueText : "N/A";
                } else if (dataType == 2) { // Number
                    if (valueNumber != null) {
                        displayValue = valueNumber.stripTrailingZeros().toPlainString();
                        
                        if (unit != null && !unit.isEmpty()) {
                            displayValue += " " + unit;
                        }
                    } else {
                        displayValue = "N/A";
                    }
                } else if (dataType == 3) { // Boolean
                    displayValue = valueText != null ? valueText : "N/A";
                } else {
                    displayValue = "N/A";
                }

                attributes.put(attrName, displayValue);
            }

            return attributes;
        } finally {
            em.close();
        }
    }
}

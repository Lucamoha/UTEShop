package com.uteshop.dao.impl.web;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.web.IProductsDao;
import com.uteshop.entity.catalog.Products;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Map;

public class ProductsDaoImpl extends AbstractDao<Products> implements IProductsDao {

	public ProductsDaoImpl() {
		super(Products.class);
	}

	@Override
	public List<Products> topLatestProducts() {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			return enma.createNamedQuery("Products.findLatestProducts", Products.class).setMaxResults(12) // tương ứng
																											// với TOP
																											// 12
					.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Products> findAll(int page, int pageSize) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			return enma.createQuery("SELECT p FROM Products p ORDER BY p.CreatedAt DESC", Products.class)
					.setFirstResult((page - 1) * pageSize) // vị trí bắt đầu
					.setMaxResults(pageSize) // số lượng record
					.getResultList();
		} finally {
			enma.close();
		}
	}

	public static void main(String[] args) {
		ProductsDaoImpl dao = new ProductsDaoImpl();
		List<Products> products = dao.getRelativeProducts(1);
		for (Products p : products) {
			System.out.println(p);
		}
	}

	@Override
	public Products findBySlug(String slug) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT p FROM Products p WHERE p.Slug = :slug";
			return enma.createQuery(jpql, Products.class).setParameter("slug", slug).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

    @Override
    public List<Products> getRelativeProducts(int productId) {
        EntityManager enma = JPAConfigs.getEntityManager();
        try {
            Integer categoryId = enma.createQuery(
                            "SELECT p.category.id FROM Products p WHERE p.id = :productId", Integer.class)
                    .setParameter("productId", productId)
                    .getSingleResult();

            if (categoryId == null) {
                return List.of();
            }

            String jpql = """
            SELECT p FROM Products p
            WHERE p.category.id = :categoryId AND p.id <> :productId
            ORDER BY p.CreatedAt DESC
            """;

            return enma.createQuery(jpql, Products.class)
                    .setParameter("categoryId", categoryId)
                    .setParameter("productId", productId)
                    .getResultList();

        } catch (NoResultException e) {
            return List.of();
        } finally {
            enma.close();
        }
    }

    @Override
	public List<Products> findByCategoryId(int catId, int page, int pageSize) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			TypedQuery<Products> query = enma.createNamedQuery("Products.findByCategoryId", Products.class);
			query.setParameter("catId", catId);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public long countByCategoryId(int catId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			TypedQuery<Long> query = enma.createNamedQuery("Products.countByCategoryId", Long.class);
			query.setParameter("catId", catId);
			return query.getSingleResult();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Products> findByCategoryIds(List<Integer> catIds, int page, int pageSize) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			TypedQuery<Products> query = enma.createNamedQuery("Products.findByCategoryIds", Products.class);
			query.setParameter("catIds", catIds);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public long countByCategoryIds(List<Integer> catIds) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			TypedQuery<Long> query = enma.createNamedQuery("Products.countByCategoryIds", Long.class);
			query.setParameter("catIds", catIds);
			return query.getSingleResult();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Products> searchAndFilter(List<Integer> categoryIds, String keyword, 
	                                       List<Integer> colorIds, java.math.BigDecimal minPrice, 
	                                       java.math.BigDecimal maxPrice, String sortBy, 
	                                       Map<Integer, Object> attributeFilters,
	                                       int page, int pageSize) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			StringBuilder jpql = new StringBuilder(
				"SELECT DISTINCT p FROM Products p " +
				"LEFT JOIN FETCH p.images pi " +
				"WHERE p.Status = true "
			);
			
			// Filter by category
			if (categoryIds != null && !categoryIds.isEmpty()) {
				jpql.append("AND p.category.id IN :categoryIds ");
			}
			
			// Search by keyword
			if (keyword != null && !keyword.trim().isEmpty()) {
				jpql.append("AND LOWER(p.Name) LIKE :keyword ");
			}
			
			// Filter by price range
			if (minPrice != null) {
				jpql.append("AND p.BasePrice >= :minPrice ");
			}
			if (maxPrice != null) {
				jpql.append("AND p.BasePrice <= :maxPrice ");
			}
			
			// Filter by attributes
			if (attributeFilters != null && !attributeFilters.isEmpty()) {
				int attrIndex = 0;
				for (Map.Entry<Integer, Object> entry : attributeFilters.entrySet()) {
					Integer attributeId = entry.getKey();
					Object value = entry.getValue();
					
					if (value instanceof List) {
						// Multiple values (TEXT or NUMBER) - use OR conditions
						@SuppressWarnings("unchecked")
						List<String> values = (List<String>) value;
						
						jpql.append("AND EXISTS (SELECT 1 FROM ProductAttributeValues pav" + attrIndex + " ");
						jpql.append("WHERE pav" + attrIndex + ".product.Id = p.Id ");
						jpql.append("AND pav" + attrIndex + ".attribute.Id = :attrId" + attrIndex + " ");
						jpql.append("AND (");
						
						// Build OR conditions for each value
						for (int i = 0; i < values.size(); i++) {
							if (i > 0) jpql.append(" OR ");
							jpql.append("(pav" + attrIndex + ".ValueText = :attrValText" + attrIndex + "_" + i + " ");
							jpql.append("OR (pav" + attrIndex + ".ValueNumber >= :attrValNumMin" + attrIndex + "_" + i + " ");
							jpql.append("AND pav" + attrIndex + ".ValueNumber <= :attrValNumMax" + attrIndex + "_" + i + "))");
						}
						
						jpql.append(")) ");
					} else if (value instanceof String) {
						// Single value (backward compatibility)
						jpql.append("AND EXISTS (SELECT 1 FROM ProductAttributeValues pav" + attrIndex + " ");
						jpql.append("WHERE pav" + attrIndex + ".product.Id = p.Id ");
						jpql.append("AND pav" + attrIndex + ".attribute.Id = :attrId" + attrIndex + " ");
						jpql.append("AND (pav" + attrIndex + ".ValueText = :attrValText" + attrIndex + " ");
						jpql.append("OR (pav" + attrIndex + ".ValueNumber >= :attrValNumMin" + attrIndex + " ");
						jpql.append("AND pav" + attrIndex + ".ValueNumber <= :attrValNumMax" + attrIndex + "))) ");
					} else if (value instanceof Map) {
						// NUMBER range filter (deprecated, kept for compatibility)
						@SuppressWarnings("unchecked")
						Map<String, Double> range = (Map<String, Double>) value;
						jpql.append("AND EXISTS (SELECT 1 FROM ProductAttributeValues pav" + attrIndex + " ");
						jpql.append("WHERE pav" + attrIndex + ".product.Id = p.Id ");
						jpql.append("AND pav" + attrIndex + ".attribute.Id = :attrId" + attrIndex + " ");
						if (range.containsKey("min")) {
							jpql.append("AND pav" + attrIndex + ".ValueNumber >= :attrMin" + attrIndex + " ");
						}
						if (range.containsKey("max")) {
							jpql.append("AND pav" + attrIndex + ".ValueNumber <= :attrMax" + attrIndex + " ");
						}
						jpql.append(") ");
					}
					attrIndex++;
				}
			}
			
			// Sorting
			if (sortBy != null) {
				switch (sortBy) {
					case "price_asc":
						jpql.append("ORDER BY p.BasePrice ASC");
						break;
					case "price_desc":
						jpql.append("ORDER BY p.BasePrice DESC");
						break;
					case "name_asc":
						jpql.append("ORDER BY p.Name ASC");
						break;
					case "name_desc":
						jpql.append("ORDER BY p.Name DESC");
						break;
					case "popularity":
						jpql.append("ORDER BY p.Sold DESC");
						break;
					default:
						jpql.append("ORDER BY p.CreatedAt DESC");
				}
			} else {
				jpql.append("ORDER BY p.CreatedAt DESC");
			}
			
			TypedQuery<Products> query = enma.createQuery(jpql.toString(), Products.class);
			
			// Set parameters
			if (categoryIds != null && !categoryIds.isEmpty()) {
				query.setParameter("categoryIds", categoryIds);
			}
			if (keyword != null && !keyword.trim().isEmpty()) {
				query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
			}
			if (minPrice != null) {
				query.setParameter("minPrice", minPrice);
			}
			if (maxPrice != null) {
				query.setParameter("maxPrice", maxPrice);
			}
			
			// Set attribute filter parameters
			if (attributeFilters != null && !attributeFilters.isEmpty()) {
				int attrIndex = 0;
				for (Map.Entry<Integer, Object> entry : attributeFilters.entrySet()) {
					Integer attributeId = entry.getKey();
					Object value = entry.getValue();
					
					query.setParameter("attrId" + attrIndex, attributeId);
					
					if (value instanceof List) {
						// Multiple values - set parameters for each value
						@SuppressWarnings("unchecked")
						List<String> values = (List<String>) value;
						
						for (int i = 0; i < values.size(); i++) {
							String strValue = values.get(i);
							query.setParameter("attrValText" + attrIndex + "_" + i, strValue);
							
							// Try to parse as BigDecimal for number comparison
							try {
								java.math.BigDecimal numValue = new java.math.BigDecimal(strValue);
								java.math.BigDecimal epsilon = new java.math.BigDecimal("0.0001");
								query.setParameter("attrValNumMin" + attrIndex + "_" + i, numValue.subtract(epsilon));
								query.setParameter("attrValNumMax" + attrIndex + "_" + i, numValue.add(epsilon));
							} catch (NumberFormatException e) {
								// Not a number, set a range that won't match
								query.setParameter("attrValNumMin" + attrIndex + "_" + i, new java.math.BigDecimal("-999999999"));
								query.setParameter("attrValNumMax" + attrIndex + "_" + i, new java.math.BigDecimal("-999999998"));
							}
						}
					} else if (value instanceof String) {
						// Single value (backward compatibility)
						String strValue = (String) value;
						query.setParameter("attrValText" + attrIndex, strValue);
						// Try to parse as BigDecimal for number comparison
						try {
							java.math.BigDecimal numValue = new java.math.BigDecimal(strValue);
							// Use a small epsilon for floating point comparison
							java.math.BigDecimal epsilon = new java.math.BigDecimal("0.0001");
							query.setParameter("attrValNumMin" + attrIndex, numValue.subtract(epsilon));
							query.setParameter("attrValNumMax" + attrIndex, numValue.add(epsilon));
						} catch (NumberFormatException e) {
							// Not a number, set a range that won't match
							query.setParameter("attrValNumMin" + attrIndex, new java.math.BigDecimal("-999999999"));
							query.setParameter("attrValNumMax" + attrIndex, new java.math.BigDecimal("-999999998"));
						}
					} else if (value instanceof Map) {
						@SuppressWarnings("unchecked")
						Map<String, Double> range = (Map<String, Double>) value;
						if (range.containsKey("min")) {
							query.setParameter("attrMin" + attrIndex, range.get("min"));
						}
						if (range.containsKey("max")) {
							query.setParameter("attrMax" + attrIndex, range.get("max"));
						}
					}
					attrIndex++;
				}
			}
			
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			
			System.out.println("DEBUG DAO - Final JPQL: " + jpql.toString());
			
			return query.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public long countSearchAndFilter(List<Integer> categoryIds, String keyword, 
	                                  List<Integer> colorIds, java.math.BigDecimal minPrice, 
	                                  java.math.BigDecimal maxPrice,
	                                  Map<Integer, Object> attributeFilters) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			StringBuilder jpql = new StringBuilder(
				"SELECT COUNT(DISTINCT p) FROM Products p " +
				"WHERE p.Status = true "
			);
			
			if (categoryIds != null && !categoryIds.isEmpty()) {
				jpql.append("AND p.category.id IN :categoryIds ");
			}
			if (keyword != null && !keyword.trim().isEmpty()) {
				jpql.append("AND LOWER(p.Name) LIKE :keyword ");
			}
			if (minPrice != null) {
				jpql.append("AND p.BasePrice >= :minPrice ");
			}
			if (maxPrice != null) {
				jpql.append("AND p.BasePrice <= :maxPrice ");
			}
			
			// Filter by attributes
			if (attributeFilters != null && !attributeFilters.isEmpty()) {
				int attrIndex = 0;
				for (Map.Entry<Integer, Object> entry : attributeFilters.entrySet()) {
					Integer attributeId = entry.getKey();
					Object value = entry.getValue();
					
					if (value instanceof List) {
						// Multiple values (TEXT or NUMBER) - use OR conditions
						@SuppressWarnings("unchecked")
						List<String> values = (List<String>) value;
						
						jpql.append("AND EXISTS (SELECT 1 FROM ProductAttributeValues pav" + attrIndex + " ");
						jpql.append("WHERE pav" + attrIndex + ".product.Id = p.Id ");
						jpql.append("AND pav" + attrIndex + ".attribute.Id = :attrId" + attrIndex + " ");
						jpql.append("AND (");
						
						// Build OR conditions for each value
						for (int i = 0; i < values.size(); i++) {
							if (i > 0) jpql.append(" OR ");
							jpql.append("(pav" + attrIndex + ".ValueText = :attrValText" + attrIndex + "_" + i + " ");
							jpql.append("OR (pav" + attrIndex + ".ValueNumber >= :attrValNumMin" + attrIndex + "_" + i + " ");
							jpql.append("AND pav" + attrIndex + ".ValueNumber <= :attrValNumMax" + attrIndex + "_" + i + "))");
						}
						
						jpql.append(")) ");
					} else if (value instanceof String) {
						// TEXT or NUMBER filter (single value)
						jpql.append("AND EXISTS (SELECT 1 FROM ProductAttributeValues pav" + attrIndex + " ");
						jpql.append("WHERE pav" + attrIndex + ".product.Id = p.Id ");
						jpql.append("AND pav" + attrIndex + ".attribute.Id = :attrId" + attrIndex + " ");
						jpql.append("AND (pav" + attrIndex + ".ValueText = :attrValText" + attrIndex + " ");
						jpql.append("OR (pav" + attrIndex + ".ValueNumber >= :attrValNumMin" + attrIndex + " ");
						jpql.append("AND pav" + attrIndex + ".ValueNumber <= :attrValNumMax" + attrIndex + "))) ");
					} else if (value instanceof Map) {
						// NUMBER range filter (deprecated, kept for compatibility)
						@SuppressWarnings("unchecked")
						Map<String, Double> range = (Map<String, Double>) value;
						jpql.append("AND EXISTS (SELECT 1 FROM ProductAttributeValues pav" + attrIndex + " ");
						jpql.append("WHERE pav" + attrIndex + ".product.Id = p.Id ");
						jpql.append("AND pav" + attrIndex + ".attribute.Id = :attrId" + attrIndex + " ");
						if (range.containsKey("min")) {
							jpql.append("AND pav" + attrIndex + ".ValueNumber >= :attrMin" + attrIndex + " ");
						}
						if (range.containsKey("max")) {
							jpql.append("AND pav" + attrIndex + ".ValueNumber <= :attrMax" + attrIndex + " ");
						}
						jpql.append(") ");
					}
					attrIndex++;
				}
			}
			
			TypedQuery<Long> query = enma.createQuery(jpql.toString(), Long.class);
			
			if (categoryIds != null && !categoryIds.isEmpty()) {
				query.setParameter("categoryIds", categoryIds);
			}
			if (keyword != null && !keyword.trim().isEmpty()) {
				query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
			}
			if (minPrice != null) {
				query.setParameter("minPrice", minPrice);
			}
			if (maxPrice != null) {
				query.setParameter("maxPrice", maxPrice);
			}
			
			// Set attribute filter parameters
			if (attributeFilters != null && !attributeFilters.isEmpty()) {
				int attrIndex = 0;
				for (Map.Entry<Integer, Object> entry : attributeFilters.entrySet()) {
					Integer attributeId = entry.getKey();
					Object value = entry.getValue();
					
					query.setParameter("attrId" + attrIndex, attributeId);
					
					if (value instanceof List) {
						// Multiple values - set parameters for each value
						@SuppressWarnings("unchecked")
						List<String> values = (List<String>) value;
						
						for (int i = 0; i < values.size(); i++) {
							String strValue = values.get(i);
							query.setParameter("attrValText" + attrIndex + "_" + i, strValue);
							
							// Try to parse as BigDecimal for number comparison
							try {
								java.math.BigDecimal numValue = new java.math.BigDecimal(strValue);
								java.math.BigDecimal epsilon = new java.math.BigDecimal("0.0001");
								query.setParameter("attrValNumMin" + attrIndex + "_" + i, numValue.subtract(epsilon));
								query.setParameter("attrValNumMax" + attrIndex + "_" + i, numValue.add(epsilon));
							} catch (NumberFormatException e) {
								// Not a number, set a range that won't match
								query.setParameter("attrValNumMin" + attrIndex + "_" + i, new java.math.BigDecimal("-999999999"));
								query.setParameter("attrValNumMax" + attrIndex + "_" + i, new java.math.BigDecimal("-999999998"));
							}
						}
					} else if (value instanceof String) {
						String strValue = (String) value;
						query.setParameter("attrValText" + attrIndex, strValue);
						// Try to parse as BigDecimal for number comparison
						try {
							java.math.BigDecimal numValue = new java.math.BigDecimal(strValue);
							// Use a small epsilon for floating point comparison
							java.math.BigDecimal epsilon = new java.math.BigDecimal("0.0001");
							query.setParameter("attrValNumMin" + attrIndex, numValue.subtract(epsilon));
							query.setParameter("attrValNumMax" + attrIndex, numValue.add(epsilon));
						} catch (NumberFormatException e) {
							// Not a number, set a range that won't match
							query.setParameter("attrValNumMin" + attrIndex, new java.math.BigDecimal("-999999999"));
							query.setParameter("attrValNumMax" + attrIndex, new java.math.BigDecimal("-999999998"));
						}
					} else if (value instanceof Map) {
						@SuppressWarnings("unchecked")
						Map<String, Double> range = (Map<String, Double>) value;
						if (range.containsKey("min")) {
							query.setParameter("attrMin" + attrIndex, range.get("min"));
						}
						if (range.containsKey("max")) {
							query.setParameter("attrMax" + attrIndex, range.get("max"));
						}
					}
					attrIndex++;
				}
			}
			
			return query.getSingleResult();
		} finally {
			enma.close();
		}
	}

	@Override
	public java.math.BigDecimal getMinPriceByCategoryIds(List<Integer> categoryIds) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT MIN(p.BasePrice) FROM Products p " +
			             "WHERE p.category.id IN :categoryIds AND p.Status = true";
			TypedQuery<java.math.BigDecimal> query = enma.createQuery(jpql, java.math.BigDecimal.class);
			query.setParameter("categoryIds", categoryIds);
			java.math.BigDecimal result = query.getSingleResult();
			return result != null ? result : java.math.BigDecimal.ZERO;
		} finally {
			enma.close();
		}
	}

	@Override
	public java.math.BigDecimal getMaxPriceByCategoryIds(List<Integer> categoryIds) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT MAX(p.BasePrice) FROM Products p " +
			             "WHERE p.category.id IN :categoryIds AND p.Status = true";
			TypedQuery<java.math.BigDecimal> query = enma.createQuery(jpql, java.math.BigDecimal.class);
			query.setParameter("categoryIds", categoryIds);
			java.math.BigDecimal result = query.getSingleResult();
			return result != null ? result : java.math.BigDecimal.ZERO;
		} finally {
			enma.close();
		}
	}
}

package com.uteshop.dao.impl.web;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.web.IProductsDao;
import com.uteshop.entity.catalog.Products;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

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
			
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			
			return query.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public long countSearchAndFilter(List<Integer> categoryIds, String keyword, 
	                                  List<Integer> colorIds, java.math.BigDecimal minPrice, 
	                                  java.math.BigDecimal maxPrice) {
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

package com.uteshop.dao.impl.Product;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.IProductsDao;
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
}

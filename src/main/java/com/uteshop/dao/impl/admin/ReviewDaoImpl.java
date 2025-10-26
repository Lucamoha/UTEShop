package com.uteshop.dao.impl.admin;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IReviewDao;
import com.uteshop.entity.engagement.Reviews;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class ReviewDaoImpl extends AbstractDao<Reviews> implements IReviewDao {
	public ReviewDaoImpl() {
		super(Reviews.class);
	}

	@Override
	public List<Reviews> findAll() {
		EntityManager enma = JPAConfigs.getEntityManager();
		return enma.createQuery("SELECT r FROM Reviews r " + "LEFT JOIN FETCH r.product " + "LEFT JOIN FETCH r.user "
				+ "LEFT JOIN FETCH r.media", Reviews.class).getResultList();
	}

	@Override
	public Reviews findById(Integer id) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			return enma
					.createQuery("SELECT r FROM Reviews r " + "LEFT JOIN FETCH r.product " + "LEFT JOIN FETCH r.user "
							+ "LEFT JOIN FETCH r.media " + "WHERE r.Id = :id", Reviews.class)
					.setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void insert(Reviews review) {
		EntityManager enma = JPAConfigs.getEntityManager();
		enma.getTransaction().begin();
		try {
			enma.persist(review);
			enma.getTransaction().commit();
		} catch (Exception e) {
			enma.getTransaction().rollback();
			throw new RuntimeException("Insert Review failed: " + e.getMessage(), e);
		} finally {
			enma.close();
		}
	}

	@Override
	public void update(Reviews review) {
		EntityManager enma = JPAConfigs.getEntityManager();
		enma.getTransaction().begin();
		try {
			enma.merge(review);
			enma.getTransaction().commit();
		} catch (Exception e) {
			enma.getTransaction().rollback();
			throw new RuntimeException("Update Review failed: " + e.getMessage(), e);
		} finally {
			enma.close();
		}
	}

	@Override
	public void delete(Integer id) {
		EntityManager enma = JPAConfigs.getEntityManager();
		enma.getTransaction().begin();
		try {
			Reviews review = enma.find(Reviews.class, id);
			if (review != null) {
				enma.remove(review);
			}
			enma.getTransaction().commit();
		} catch (Exception e) {
			enma.getTransaction().rollback();
			throw new RuntimeException("Delete Review failed: " + e.getMessage(), e);
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Reviews> findByProductId(Integer productId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		return enma
				.createQuery("SELECT r FROM Reviews r " + "LEFT JOIN FETCH r.user " + "LEFT JOIN FETCH r.media "
						+ "WHERE r.product.Id = :productId", Reviews.class)
				.setParameter("productId", productId).getResultList();
	}

	@Override
	public List<Reviews> findByUserId(Integer userId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		return enma.createQuery("SELECT r FROM Reviews r " + "LEFT JOIN FETCH r.product " + "LEFT JOIN FETCH r.media "
				+ "WHERE r.user.Id = :userId", Reviews.class).setParameter("userId", userId).getResultList();
	}

	@Override
	public List<Reviews> findByRating(Integer rating) {
		EntityManager enma = JPAConfigs.getEntityManager();
		String jpql = """

				SELECT r
				FROM Reviews r
				      LEFT JOIN FETCH r.product
				      LEFT JOIN FETCH r.user
				      LEFT JOIN FETCH r.media

				     """;
		if (rating != 0) {
			jpql += " WHERE r.Rating = :rating";
			return enma.createQuery(jpql, Reviews.class).setParameter("rating", rating).getResultList();
		}
		return enma.createQuery(jpql, Reviews.class).getResultList();
	}
}
package com.uteshop.dao;

import java.util.ArrayList;
import java.util.List;

import com.uteshop.configs.JPAConfigs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public abstract class AbstractDao<T> {
	private Class<T> entityClass;

	public AbstractDao(Class<T> cls) {
		this.entityClass = cls;
	}

	public void insert(T entity) {
		EntityManager enma = JPAConfigs.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			enma.persist(entity);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			throw e;
		} finally {
			enma.close();
		}
	}

	public void update(T entity) {
		EntityManager enma = JPAConfigs.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			enma.merge(entity);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			throw e;
		} finally {
			enma.close();
		}
	}

	public void delete(Object id) {
		EntityManager enma = JPAConfigs.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			T entity = enma.find(entityClass, id);
			enma.remove(entity);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			throw e;
		} finally {
			enma.close();
		}
	}

	public T findById(Object id) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			return enma.find(entityClass, id);
		} finally {
			enma.close();
		}
	}

	public List<T> findAll() {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			CriteriaQuery cq = enma.getCriteriaBuilder().createQuery();
			cq.select(cq.from(entityClass));
			return enma.createQuery(cq).getResultList();
		} finally {
			enma.close();
		}
	}

	public Long countAll() {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			// tao truy van tu entityclass
			CriteriaQuery cq = enma.getCriteriaBuilder().createQuery();
			Root<T> rt = cq.from(entityClass);
			cq.select(enma.getCriteriaBuilder().count(rt));
			Query q = enma.createQuery(cq);
			return (Long) q.getSingleResult();
		} finally {
			enma.close();
		}
	}

	public List<T> findAll(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName) {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(entityClass);
			Root<T> root = cq.from(entityClass);
			cq.select(root);

			if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
				String pattern = "%" + searchKeyword.toLowerCase() + "%";
				Predicate pName = cb.like(cb.lower(root.get(searchKeywordColumnName).as(String.class)), pattern);
				cq.where(pName);
			}

			TypedQuery<T> q = em.createQuery(cq);
			if (!all) {
				q.setFirstResult(firstResult);
				q.setMaxResults(maxResult);
			}
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	public List<T> findAllFetchParent(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName, String fetchColumnName) {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(entityClass);
			Root<T> root = cq.from(entityClass);

			// Nạp cột parent nếu entity có field này
			try {
				root.fetch(fetchColumnName, JoinType.LEFT);
			} catch (IllegalArgumentException e) {
				// Entity không có thuộc tính cần nạp -> bỏ qua
			}

			// cq.select(root).distinct(true); // tránh trùng dòng khi JOIN FETCH

			// Tìm kiếm theo tên
			if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
				String pattern = "%" + searchKeyword.toLowerCase() + "%";
				Predicate pName = cb.like(cb.lower(root.get(searchKeywordColumnName).as(String.class)), pattern);
				cq.where(pName);
			}

			TypedQuery<T> q = em.createQuery(cq);
			if (!all) {
				q.setFirstResult(firstResult);
				q.setMaxResults(maxResult);
			}

			return q.getResultList();
		} finally {
			em.close();
		}
	}

	public int count(String searchKeyword, String searchKeywordColumnName) {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<T> root = cq.from(entityClass);
			cq.select(cb.count(root));

			if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
				String pattern = "%" + searchKeyword.toLowerCase() + "%";
				Predicate pName = cb.like(cb.lower(root.get(searchKeywordColumnName).as(String.class)), pattern);
				cq.where(pName);
			}

			TypedQuery<Long> q = em.createQuery(cq);
			Long total = q.getSingleResult();
			return total == null ? 0 : total.intValue();
		} finally {
			em.close();
		}
	}

	public List<T> findByColumnContainingWord(String columnName, String word) {
		List<T> list = new ArrayList<>();
		if (word == null || word.trim().isEmpty()) {
			list = this.findAll();
		} else {
			EntityManager enma = JPAConfigs.getEntityManager();
			try {
				String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + columnName.trim()
						+ " LIKE :word";
				list = enma.createQuery(jpql, entityClass).setParameter("word", "%" + word.toLowerCase() + "%")
						.getResultList();
			} finally {
				enma.close();
			}
		}
		return list;
	}
}

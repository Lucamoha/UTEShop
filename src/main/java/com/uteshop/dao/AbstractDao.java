package com.uteshop.dao;

import java.util.ArrayList;
import java.util.List;

import com.uteshop.configs.JPAConfigs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.FetchParent;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public abstract class AbstractDao<T> {
	protected Class<T> entityClass;

	public AbstractDao(Class<T> cls) {
		this.entityClass = cls;
	}

	public void insert(T entity) {
		EntityManager enma = JPAConfigs.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();

			enma.persist(entity);
			enma.flush(); // ép Hibernate sinh ID

			trans.commit();
		} catch (Exception e) {
			if (trans.isActive())
				trans.rollback();
			throw e;
		} finally {
			enma.close();
		}
	}
	
	// Overloaded method sử dụng EntityManager được truyền vào (để quản lý transaction từ bên ngoài)
	public void insert(T entity, EntityManager em) {
		try {
			em.persist(entity);
			em.flush(); // ép Hibernate sinh ID
		} catch (Exception e) {
			throw e;
		}
	}

	// Dành cho entity có liên kết (VariantOptions, ProductVariants,...)
	public void insertByMerge(T entity) {
		EntityManager enma = JPAConfigs.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();

			T managedEntity = enma.merge(entity); // merge trả về bản managed
			enma.flush(); // ép ghi DB để sinh ID

			trans.commit();
		} catch (Exception e) {
			if (trans.isActive())
				trans.rollback();
			throw e;
		} finally {
			enma.close();
		}
	}
	
	// Overloaded method với EntityManager được truyền vào
	public void insertByMerge(T entity, EntityManager em) {
		try {
			T managedEntity = em.merge(entity);
			em.flush();
		} catch (Exception e) {
			throw e;
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
	
	// Overloaded method với EntityManager được truyền vào
	public void update(T entity, EntityManager em) {
		try {
			em.merge(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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
			if (trans.isActive())
				trans.rollback();
			Throwable cause = e.getCause();
	        if (cause instanceof org.hibernate.exception.ConstraintViolationException
	            || (cause != null && cause.getMessage() != null && cause.getMessage().contains("constraint"))) {
	            throw new RuntimeException("FOREIGN_KEY_CONSTRAINT");
	        }

	        throw new RuntimeException(e);
		} finally {
			enma.close();
		}
	}
	
	// Overloaded method với EntityManager được truyền vào
	public void delete(Object id, EntityManager em) {
		try {
			T entity = em.find(entityClass, id);
			if (entity != null) {
				em.remove(entity);
			}
		} catch (Exception e) {
			Throwable cause = e.getCause();
	        if (cause instanceof org.hibernate.exception.ConstraintViolationException
	            || (cause != null && cause.getMessage() != null && cause.getMessage().contains("constraint"))) {
	            throw new RuntimeException("FOREIGN_KEY_CONSTRAINT");
	        }
	        throw new RuntimeException(e);
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
	
	// Overloaded method với EntityManager được truyền vào
	public T findById(Object id, EntityManager em) {
		return em.find(entityClass, id);
	}

	public T findByIdFetchColumn(Object id, String column) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = String.format("SELECT e FROM %s e LEFT JOIN FETCH e.%s WHERE e.id = :id",
					entityClass.getSimpleName(), column);
			return enma.createQuery(jpql, entityClass).setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			enma.close();
		}
	}

	public T findByIdFetchColumns(Object id, List<String> fetchColumnsName) {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(entityClass);
			Root<T> root = cq.from(entityClass);

			// Thêm fetch cho các cột quan hệ
			if (fetchColumnsName != null) {
				try {
					for (String column : fetchColumnsName) {
						if (column.contains(".")) {
							// fetch sâu, ví dụ "categoryAttributes.attribute"
							String[] parts = column.split("\\.");
							FetchParent<?, ?> fetchParent = root;
							for (String part : parts) {
								fetchParent = fetchParent.fetch(part, JoinType.LEFT);
							}
						} else {
							root.fetch(column, JoinType.LEFT);
						}
					}

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}

			// WHERE e.Id = :id
			// Cần tìm tên cột Id đúng
			cq.select(root).where(cb.equal(root.get("Id"), id));

			TypedQuery<T> query = em.createQuery(cq);
			return query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public T findByIdFetchColumns(Object id, int firstResult, int maxResult, List<String> fetchColumnsName) {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(entityClass);
			Root<T> root = cq.from(entityClass);

			// Thêm fetch cho các cột quan hệ
			if (fetchColumnsName != null) {
				try {
					for (String column : fetchColumnsName) {
						if (column.contains(".")) {
							// fetch sâu, ví dụ "categoryAttributes.attribute"
							String[] parts = column.split("\\.");
							FetchParent<?, ?> fetchParent = root;
							for (String part : parts) {
								fetchParent = fetchParent.fetch(part, JoinType.LEFT);
							}
						} else {
							root.fetch(column, JoinType.LEFT);
						}
					}

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}

			// WHERE e.Id = :id
			// Cần tìm tên cột Id đúng
			cq.select(root).where(cb.equal(root.get("Id"), id));

			TypedQuery<T> query = em.createQuery(cq);
			query.setFirstResult(firstResult);
			query.setMaxResults(maxResult);
			return query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
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

			// Nạp cột nếu entity có field này
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

	public List<T> findAllFetchColumns(List<String> fetchColumnsName) {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(entityClass);
			Root<T> root = cq.from(entityClass);

			// Nạp cột nếu entity có field này
			try {
				for (String column : fetchColumnsName) {
					if (column.contains(".")) {
						// fetch sâu, ví dụ "categoryAttributes.attribute"
						String[] parts = column.split("\\.");
						FetchParent<?, ?> fetchParent = root;
						for (String part : parts) {
							fetchParent = fetchParent.fetch(part, JoinType.LEFT);
						}
					} else {
						root.fetch(column, JoinType.LEFT);
					}
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			// cq.select(root).distinct(true); // tránh trùng dòng khi JOIN FETCH
			TypedQuery<T> q = em.createQuery(cq);

			return q.getResultList();
		} finally {
			em.close();
		}
	}

	public List<T> findAllFetchColumns(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName, List<String> fetchColumnsName) {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(entityClass);
			Root<T> root = cq.from(entityClass);

			// Nạp cột nếu entity có field này
			try {
				for (String column : fetchColumnsName) {
					if (column.contains(".")) {
						// fetch sâu, ví dụ "categoryAttributes.attribute"
						String[] parts = column.split("\\.");
						FetchParent<?, ?> fetchParent = root;
						for (String part : parts) {
							fetchParent = fetchParent.fetch(part, JoinType.LEFT);
						}
					} else {
						root.fetch(column, JoinType.LEFT);
					}
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
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

	public List<T> findByColumnHasExactWord(String columnName, String word) {
		List<T> list = new ArrayList<>();
		if (word == null || word.trim().isEmpty()) {
			list = this.findAll();
		} else {
			EntityManager enma = JPAConfigs.getEntityManager();
			try {
				String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + columnName.trim()
						+ " = :word";
				list = enma.createQuery(jpql, entityClass).setParameter("word", word.toLowerCase()).getResultList();
			} finally {
				enma.close();
			}
		}
		return list;
	}
}

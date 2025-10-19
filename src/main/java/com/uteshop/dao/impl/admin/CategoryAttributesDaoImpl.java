package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.ICategoryAttributeDao;
import com.uteshop.entity.catalog.CategoryAttributes;
import com.uteshop.entity.catalog.CategoryAttributes.Id;

import jakarta.persistence.EntityManager;

public class CategoryAttributesDaoImpl extends AbstractDao<CategoryAttributes> implements ICategoryAttributeDao {

	public CategoryAttributesDaoImpl() {
		super(CategoryAttributes.class);
	}

	@Override
	public List<CategoryAttributes> findByCategoryId(int categoryId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		String jpql = """
				SELECT ca
				FROM CategoryAttributes ca
				WHERE ca.category.Id = :categoryId
				""";
		return enma.createQuery(jpql, CategoryAttributes.class).setParameter("categoryId", categoryId).getResultList();
	}

	@Override
	public CategoryAttributes findById(Id id) {
		return super.findById(id);
	}

	@Override
	public void insert(CategoryAttributes entity) {
		super.insertByMerge(entity);
	}

	@Override
	public void update(CategoryAttributes entity) {
		super.update(entity);
	}

	@Override
	public CategoryAttributes findByCategoryIdAndAttributeId(int categoryId, int attributeId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		String jpql = """
					SELECT ca
					FROM CategoryAttributes ca
					WHERE ca.category.Id = :categoryId
					AND ca.attribute.Id = :attributeId
				""";
		List<CategoryAttributes> categoryAttributes = enma.createQuery(jpql, CategoryAttributes.class)
				.setParameter("categoryId", categoryId).setParameter("attributeId", attributeId).getResultList();
		return categoryAttributes.isEmpty() ? null : categoryAttributes.get(0);
	}

	@Override
	public CategoryAttributes findByIdFetchColumns(Object id, int firstResult, int maxResult,
			List<String> fetchColumnsName) {
		return super.findByIdFetchColumns(id, firstResult, maxResult, fetchColumnsName);
	}

	@Override
	public boolean existsById(Id id) {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
			CategoryAttributes ca = em.find(CategoryAttributes.class, id);
			return ca != null;
		} finally {
			em.close();
		}
	}
}

package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IProductsDao;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.ProductAttributeValues;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.Products;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class ProductsDaoImpl extends AbstractDao<Products> implements IProductsDao {
	public ProductsDaoImpl() {
		super(Products.class);
	}

	@Override
	public List<Object[]> getTopSellingProducts(int limit, int branchId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = """
						SELECT ot.product.Name, SUM(ot.Quantity) AS totalSold
						FROM OrderItems ot

					""";
			if (branchId != 0) {
				jpql += " WHERE ot.order.branch.Id = :branchId";
			}
			jpql += """

					 	 GROUP BY ot.product.Name
						ORDER BY totalSold DESC
					""";
			if (branchId != 0) {
				return enma.createQuery(jpql, Object[].class).setParameter("branchId", branchId).setMaxResults(limit)
						.getResultList();
			} else {
				return enma.createQuery(jpql, Object[].class).setMaxResults(limit).getResultList();
			}
		} finally {
			enma.close();
		}
	}

	@Override
	public Products findByName(String name) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT p FROM Products p WHERE p.Name = :name";
			return enma.createQuery(jpql, Products.class).setParameter("name", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Attributes> findAttributesByCategoryId(Integer categoryId) {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
			return em.createQuery("""
					    SELECT DISTINCT a
					    FROM CategoryAttributes ca
					    JOIN ca.attribute a
					    WHERE ca.category.Id = :categoryId
					""", Attributes.class).setParameter("categoryId", categoryId).getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public void deleteVariantOptionsByVariantsId(int variantId, EntityManager enma) {
		String deleteOptionsJpql = "DELETE FROM VariantOptions vo WHERE vo.variant.id = :variantId";
		int deletedOptions = enma.createQuery(deleteOptionsJpql).setParameter("variantId", variantId).executeUpdate();
		System.out.println("Đã xóa " + deletedOptions + " variant options cho variant ID: " + variantId);
	}

	@Override
	public void deleteProductAttributeValueByProductIdAndAttributeId(int productId, int attributeId,
			EntityManager enma) {
		String jpql = "DELETE FROM ProductAttributeValues pav WHERE pav.product.Id = :productId AND pav.attribute.Id = :attributeId";
		enma.createQuery(jpql).setParameter("productId", productId).setParameter("attributeId", attributeId)
				.executeUpdate();
	}

	@Override
	public ProductVariants findVariantBySKU(EntityManager enma, String sku, List<Integer> deletedIds) {
		String jpql = "SELECT pv FROM ProductVariants pv WHERE pv.SKU = :sku";
		if (deletedIds != null && !deletedIds.isEmpty()) {
			jpql += " AND pv.Id NOT IN :deletedIds"; // loại trừ deleted variants
		}

		var query = enma.createQuery(jpql, ProductVariants.class).setParameter("sku", sku);

		if (deletedIds != null && !deletedIds.isEmpty()) {
			query.setParameter("deletedIds", deletedIds);
		}

		return query.getSingleResult();
	}

	@Override
	public List<Integer> getAttributeIdsByCurrentCategoryOfProducts(int categoryId, EntityManager enma) {
		String jpql = "SELECT ca.id.attributeId FROM CategoryAttributes ca WHERE ca.id.categoryId = :categoryId";
		return enma.createQuery(jpql, Integer.class).setParameter("categoryId", categoryId).getResultList();
	}

	@Override
	public void deleteAllProductAttributeValuesByProductId(int productId, EntityManager enma) {
		String deleteJpql = "DELETE FROM ProductAttributeValues pav WHERE pav.product.Id = :productId";
		enma.createQuery(deleteJpql).setParameter("productId", productId).executeUpdate();
	}

	@Override
	public void deleteProductAttributeValuesNotInNewCategoryOfProduct(int productId, List<Integer> validIds,
			EntityManager enma) {
		String deleteJpql = "DELETE FROM ProductAttributeValues pav WHERE pav.product.Id = :productId AND pav.attribute.Id NOT IN :validIds";
		enma.createQuery(deleteJpql).setParameter("productId", productId).setParameter("validIds", validIds)
				.executeUpdate();
	}

	@Override
	public ProductAttributeValues findProductAttributeValueByProductIdAndAttributeId(int productId, int attributeId,
			EntityManager enma) {
		String jpql = "SELECT pav FROM ProductAttributeValues pav WHERE pav.product.Id = :productId AND pav.attribute.Id = :attributeId";
		return enma.createQuery(jpql, ProductAttributeValues.class).setParameter("productId", productId)
				.setParameter("attributeId", attributeId).getSingleResult();
	}
}

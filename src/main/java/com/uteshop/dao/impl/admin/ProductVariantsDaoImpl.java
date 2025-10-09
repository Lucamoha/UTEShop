package com.uteshop.dao.impl.admin;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.admin.IProductVariantsDao;

import jakarta.persistence.EntityManager;

public class ProductVariantsDaoImpl implements IProductVariantsDao {

	@Override
	public long getLowStockCount(int threshold) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT COUNT(v.Id) FROM ProductVariants v WHERE (SELECT COALESCE(SUM(bi.BranchStock), 0) FROM BranchInventory bi WHERE bi.variant.Id = v.Id) < :threshold";
			return enma.createQuery(jpql, Long.class).setParameter("threshold", threshold).getSingleResult();
		} finally {
			enma.close();
		}
	}

}

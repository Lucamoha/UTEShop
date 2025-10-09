package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.admin.IProductVariantsDao;

import jakarta.persistence.EntityManager;

public class ProductVariantsDaoImpl implements IProductVariantsDao {

	@Override
	public long getLowStockCount(int threshold) {
		/*
		 * EntityManager enma = JPAConfigs.getEntityManager(); try { String jpql =
		 * "SELECT COUNT(v.Id) FROM ProductVariants v WHERE (SELECT COALESCE(SUM(bi.BranchStock), 0) FROM BranchInventory bi WHERE bi.variant.Id = v.Id) < :threshold"
		 * ; return enma.createQuery(jpql, Long.class).setParameter("threshold",
		 * threshold).getSingleResult(); } finally { enma.close(); }
		 */
		
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String sql = """
	
					SELECT COUNT(*)
					FROM(
						SELECT p.Id, SUM(bi.BranchStock) AS TotalStock
						FROM Products p 
						JOIN ProductVariants pv ON pv.ProductId = p.Id 
						JOIN BranchInventory bi ON bi.VariantId = pv.Id 
						WHERE p.Status = 1 
						GROUP BY p.Id 
						HAVING SUM(bi.BranchStock) <= :threshold 
					) AS Q
					""";
			return ((Number)enma.createNativeQuery(sql).setParameter("threshold", threshold).getSingleResult()).longValue();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Object[]> getLowStockProducts(int limit, int threshold) {
		EntityManager enma = JPAConfigs.getEntityManager();
		//threshold = 5;
		try {
			String sql = """
					SELECT TOP(:limit) 
						p.Id AS ProductId, 
						p.Name AS ProductName, 
						SUM(bi.BranchStock) AS TotalStock 
					FROM Products p 
					JOIN ProductVariants pv ON pv.ProductId = p.Id 
					JOIN BranchInventory bi ON bi.VariantId = pv.Id 
					WHERE p.Status = 1 
					GROUP BY p.Id, p.Name 
					HAVING SUM(bi.BranchStock) <= :threshold
					ORDER BY SUM(bi.BranchStock) ASC
					""";
			return enma.createNativeQuery(sql).setParameter("limit", limit).setParameter("threshold", threshold).getResultList();
		} finally {
			enma.close();
		} 
	}

}

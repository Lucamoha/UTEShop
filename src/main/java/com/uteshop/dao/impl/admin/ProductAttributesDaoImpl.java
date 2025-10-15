package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.admin.IProductAttributesDao;
import com.uteshop.dto.admin.ProductAttributeDisplayModel;

import jakarta.persistence.EntityManager;

public class ProductAttributesDaoImpl implements IProductAttributesDao {

	@Override
	public List<ProductAttributeDisplayModel> getAttributesByProductId(int productId) {

		EntityManager enma = JPAConfigs.getEntityManager();
		String jpql = """
					SELECT new ProductAttributeDisplayModel(
						pav.product.Id,
						pav.attribute.Id,
						a.Name,
						a.DataType,
						a.Unit,
						pav.ValueText,
						pav.ValueNumber
					)
					FROM ProductAttributeValues pav
					JOIN pav.attribute a
					WHERE pav.product.Id = :productId
				""";
		return enma.createQuery(jpql, ProductAttributeDisplayModel.class).setParameter("productId", productId)
				.getResultList();
	}

}

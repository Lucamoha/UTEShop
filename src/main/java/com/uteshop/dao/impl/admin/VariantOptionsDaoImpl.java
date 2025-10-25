package com.uteshop.dao.impl.admin;

import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IVariantOptionsDao;
import com.uteshop.entity.catalog.VariantOptions;

import jakarta.persistence.EntityManager;

public class VariantOptionsDaoImpl extends AbstractDao<VariantOptions> implements IVariantOptionsDao {

	public VariantOptionsDaoImpl() {
		super(VariantOptions.class);
	}

	@Override
	public void deleteByProductVariantId(int id, EntityManager em) {
		String jpql = "DELETE FROM VariantOptions vo WHERE vo.variant.id = :variantId";
		em.createQuery(jpql).setParameter("variantId", id)
				.executeUpdate();
	}

}

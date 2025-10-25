package com.uteshop.dao.admin;

import jakarta.persistence.EntityManager;

public interface IVariantOptionsDao {
	void deleteByProductVariantId(int id, EntityManager em);
}

package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IOptionValuesDao;
import com.uteshop.entity.catalog.OptionValues;

import jakarta.persistence.EntityManager;

public class OptionValuesDaoImpl extends AbstractDao<OptionValues> implements IOptionValuesDao {

	public OptionValuesDaoImpl() {
		super(OptionValues.class);
	}
	
	@Override
	public List<OptionValues> findByOptionTypeId(int optionTypeId) {
		EntityManager em = JPAConfigs.getEntityManager();
	    try {
	        return em.createQuery("""
	            SELECT v
	            FROM OptionValues v
	            WHERE v.optionType.id = :optionTypeId
	        """, OptionValues.class)
	        .setParameter("optionTypeId", optionTypeId)
	        .getResultList();
	    } finally {
	        em.close();
	    }
	}
}

package com.uteshop.dao.impl.admin;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IAttributesDao;
import com.uteshop.entity.catalog.Attributes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class AttributesDaoImpl extends AbstractDao<Attributes> implements IAttributesDao {

	public AttributesDaoImpl() {
		super(Attributes.class);
	}

	@Override
	public List<Attributes> findAll() {
		EntityManager enma = JPAConfigs.getEntityManager();
		TypedQuery<Attributes> query = enma.createNamedQuery("Attributes.findAll", Attributes.class);
		return query.getResultList();
	}
}

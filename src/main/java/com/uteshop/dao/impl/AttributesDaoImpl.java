package com.uteshop.dao.impl;

import java.util.List;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.IAttributesDao;
import com.uteshop.entities.Attributes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class AttributesDaoImpl implements IAttributesDao {

	@Override
	public List<Attributes> findAll() {
		EntityManager enma = JPAConfigs.getEntityManager();
		TypedQuery<Attributes> query = enma.createNamedQuery("Attributes.findAll", Attributes.class);
		return query.getResultList();
	}
}

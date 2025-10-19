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
	
	@Override
	public boolean existsInCategoryAttributes(int attributeId) {
	    EntityManager em = JPAConfigs.getEntityManager();
	    try {
	        Long count = em.createQuery(
	            "SELECT COUNT(ca) FROM CategoryAttributes ca WHERE ca.attribute.Id = :attrId", Long.class)
	            .setParameter("attrId", attributeId)
	            .getSingleResult();
	        return count > 0;
	    } finally {
	        em.close();
	    }
	}

	@Override
	public boolean existsInProductAttributeValues(int attributeId) {
	    EntityManager em = JPAConfigs.getEntityManager();
	    try {
	        Long count = em.createQuery(
	            "SELECT COUNT(pav) FROM ProductAttributeValues pav WHERE pav.attribute.Id = :attrId", Long.class)
	            .setParameter("attrId", attributeId)
	            .getSingleResult();
	        return count > 0;
	    } finally {
	        em.close();
	    }
	}

	@Override
	public List<Attributes> findAllFetchColumns(List<String> fetchColumnsName) {
		return super.findAllFetchColumns(fetchColumnsName);
	}
}

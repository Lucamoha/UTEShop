package com.uteshop.dao.impl.admin;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.entity.branch.Branches;

import jakarta.persistence.EntityManager;

public class BranchesDaoImpl extends AbstractDao<Branches> {

	public BranchesDaoImpl() {
		super(Branches.class);
	}
	
	public Long countInventory(int branchId) {
	    EntityManager em = JPAConfigs.getEntityManager();
	    try {
	        Long count = em.createQuery(
	            "SELECT COUNT(bi) FROM BranchInventory bi WHERE bi.branch.Id = :branchId", Long.class)
	            .setParameter("branchId", branchId)
	            .getSingleResult();
	        return count;
	    } finally {
	        em.close();
	    }
	}
	
	public boolean existsInInventory(int branchId) {
		return this.countInventory(branchId) > 0;
	}
}

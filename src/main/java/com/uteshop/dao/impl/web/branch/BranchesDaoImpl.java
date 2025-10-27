package com.uteshop.dao.impl.web.branch;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.web.branch.IBranchesDao;
import com.uteshop.entity.branch.Branches;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BranchesDaoImpl implements IBranchesDao {
    
    @Override
    public List<Branches> findAllActiveBranches() {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            String jpql = """
                SELECT b FROM Branches b
                WHERE b.IsActive = true
                ORDER BY b.Name
            """;
            TypedQuery<Branches> query = em.createQuery(jpql, Branches.class);
            
            // Add hints to prevent deadlock
            query.setHint("jakarta.persistence.query.timeout", 5000);
            query.setHint("jakarta.persistence.lock.timeout", 3000);
            query.setHint("jakarta.persistence.cache.retrieveMode", "BYPASS");
            query.setHint("org.hibernate.readOnly", true);
            
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                try {
                    em.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public Branches findById(Integer id) {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            return em.find(Branches.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                try {
                    em.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public Integer getBranchStock(Integer branchId, Integer variantId) {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            String jpql = """
                SELECT bi.BranchStock FROM BranchInventory bi
                WHERE bi.id.branchId = :branchId
                AND bi.id.variantId = :variantId
            """;
            TypedQuery<Integer> query = em.createQuery(jpql, Integer.class);
            query.setParameter("branchId", branchId);
            query.setParameter("variantId", variantId);
            
            // Add hints to prevent deadlock
            query.setHint("jakarta.persistence.query.timeout", 5000);
            query.setHint("jakarta.persistence.lock.timeout", 3000);
            query.setHint("jakarta.persistence.cache.retrieveMode", "BYPASS");
            query.setHint("org.hibernate.readOnly", true);
            
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                // Variant không có trong chi nhánh này
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (em != null && em.isOpen()) {
                try {
                    em.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public Map<Integer, Integer> getBranchStockBulk(Integer branchId, List<Integer> variantIds) {
        Map<Integer, Integer> stockMap = new HashMap<>();
        
        if (branchId == null || variantIds == null || variantIds.isEmpty()) {
            return stockMap;
        }
        
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            
            // Use IN clause to fetch all stocks in one query
            String jpql = """
                SELECT bi.id.variantId, bi.BranchStock 
                FROM BranchInventory bi
                WHERE bi.id.branchId = :branchId
                AND bi.id.variantId IN :variantIds
            """;
            
            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setParameter("branchId", branchId);
            query.setParameter("variantIds", variantIds);
            
            // Add hints to prevent deadlock
            query.setHint("jakarta.persistence.query.timeout", 5000);
            query.setHint("jakarta.persistence.lock.timeout", 3000);
            query.setHint("jakarta.persistence.cache.retrieveMode", "BYPASS");
            query.setHint("org.hibernate.readOnly", true);
            
            List<Object[]> results = query.getResultList();
            
            // Build map from results
            for (Object[] row : results) {
                Integer variantId = (Integer) row[0];
                Integer stock = (Integer) row[1];
                stockMap.put(variantId, stock);
            }
            
            // Fill in 0 for variants not found in inventory
            for (Integer variantId : variantIds) {
                if (!stockMap.containsKey(variantId)) {
                    stockMap.put(variantId, 0);
                }
            }
            
            return stockMap;
        } catch (Exception e) {
            e.printStackTrace();
            // Return 0 stock for all variants on error
            for (Integer variantId : variantIds) {
                stockMap.put(variantId, 0);
            }
            return stockMap;
        } finally {
            if (em != null && em.isOpen()) {
                try {
                    em.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.uteshop.dao.impl.web;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.web.IVouchersDao;
import com.uteshop.entity.cart.Vouchers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;

public class VouchersDaoImpl extends AbstractDao<Vouchers> implements IVouchersDao {
    
    public VouchersDaoImpl() {
        super(Vouchers.class);
    }

    @Override
    public Vouchers findByCode(String code) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = "SELECT v FROM Vouchers v WHERE UPPER(v.Code) = UPPER(:code)";
            return em.createQuery(jpql, Vouchers.class)
                    .setParameter("code", code)
                    .setHint("jakarta.persistence.query.timeout", 5000) // 5 seconds timeout
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void incrementUsage(Integer voucherId) {
        EntityManager em = null;
        EntityTransaction trans = null;
        try {
            em = JPAConfigs.getEntityManager();
            trans = em.getTransaction();
            trans.begin();
            
            Vouchers voucher = em.find(Vouchers.class, voucherId);
            if (voucher != null) {
                voucher.setTotalUsed(voucher.getTotalUsed() + 1);
                em.merge(voucher);
            }
            
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans != null && trans.isActive()) {
                try {
                    trans.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            if (em != null && em.isOpen()) {
                try {
                    em.close();
                } catch (Exception closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Vouchers> findAvailableVouchers() {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = "SELECT v FROM Vouchers v " +
                         "WHERE v.IsActive = true " +
                         "AND v.StartsAt <= CURRENT_TIMESTAMP " +
                         "AND v.EndsAt >= CURRENT_TIMESTAMP " +
                         "AND v.TotalUsed < v.MaxUses " +
                         "ORDER BY v.Type ASC, v.Value DESC";
            
            return em.createQuery(jpql, Vouchers.class)
                    .setHint("jakarta.persistence.query.timeout", 5000)
                    .setHint("jakarta.persistence.cache.retrieveMode", "BYPASS") // Bypass cache
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}

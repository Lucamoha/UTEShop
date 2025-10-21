package com.uteshop.dao.impl.admin;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IVoucherDao;
import com.uteshop.entity.cart.Vouchers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class VoucherDaoImpl extends AbstractDao<Vouchers> implements IVoucherDao {
    public VoucherDaoImpl() {
        super(Vouchers.class);
    }

    // Giữ override nếu AbstractDao không đầy đủ; loại bỏ catch không cần ở findById

    @Override
    public List<Vouchers> findAll() {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            return em.createQuery("SELECT v FROM Vouchers v", Vouchers.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Vouchers findById(Integer id) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            return em.find(Vouchers.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void insert(Vouchers voucher) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(voucher);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to insert voucher: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Vouchers voucher) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            em.getTransaction().begin();
            Vouchers existingVoucher = em.find(Vouchers.class, voucher.getId());
            if (existingVoucher == null) {
                throw new RuntimeException("Voucher not found with ID: " + voucher.getId());
            }
            em.merge(voucher);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update voucher: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            em.getTransaction().begin();
            Vouchers voucher = em.find(Vouchers.class, id);
            if (voucher == null) {
                throw new RuntimeException("Voucher not found with ID: " + id);
            }
            em.remove(voucher);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete voucher: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    @Override
    public Vouchers findByCode(String code) {
        EntityManager em = JPAConfigs.getEntityManager();
        TypedQuery<Vouchers> query = em.createQuery("SELECT v FROM Vouchers v WHERE v.Code = :code", Vouchers.class);
        query.setParameter("code", code);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
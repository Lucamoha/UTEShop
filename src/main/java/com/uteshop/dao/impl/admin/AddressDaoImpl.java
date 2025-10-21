package com.uteshop.dao.impl.admin;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IAddressDao;
import com.uteshop.entity.auth.Addresses;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class AddressDaoImpl extends AbstractDao<Addresses> implements IAddressDao {

    public AddressDaoImpl()
    {
        super(Addresses.class);
    }
    @PersistenceContext
    private EntityManager entityManager;
    public void save(Addresses address) {
        EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            if (address.getId() == null) {
                em.persist(address);
            } else {
                address = em.merge(address);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
    @Override
    public List<Addresses> findByUserId(Integer userId) {
        return entityManager.createQuery(
                "SELECT a FROM Addresses a WHERE a.user.id = :userId", Addresses.class
        ).setParameter("userId", userId).getResultList();
    }
    @Override
    public List<Addresses> findByRole(String role) {
        EntityManager em = JPAConfigs.getEntityManager();  // Thêm dòng này
        try {
            return em.createQuery(
                    "SELECT a FROM Addresses a WHERE a.user.UserRole = :role", Addresses.class
            ).setParameter("role", role).getResultList();
        } finally {
            em.close();  // Quan trọng: Đóng EM sau dùng
        }
    }
}

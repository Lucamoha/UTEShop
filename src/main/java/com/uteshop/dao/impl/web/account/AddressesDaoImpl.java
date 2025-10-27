package com.uteshop.dao.impl.web.account;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.web.account.IAddressesDao;
import com.uteshop.entity.auth.Addresses;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AddressesDaoImpl implements IAddressesDao {
    
    @Override
    public List<Addresses> findByUserId(Integer userId) {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            String jpql = """
                SELECT a FROM Addresses a
                WHERE a.user.Id = :userId
                ORDER BY a.IsDefault DESC, a.Id DESC
            """;
            TypedQuery<Addresses> query = em.createQuery(jpql, Addresses.class);
            query.setParameter("userId", userId);
            
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
    public Addresses findDefaultAddress(Integer userId) {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            String jpql = """
                SELECT a FROM Addresses a
                WHERE a.user.Id = :userId
                AND a.IsDefault = true
            """;
            TypedQuery<Addresses> query = em.createQuery(jpql, Addresses.class);
            query.setParameter("userId", userId);
            
            // Add hints to prevent deadlock
            query.setHint("jakarta.persistence.query.timeout", 5000);
            query.setHint("jakarta.persistence.lock.timeout", 3000);
            query.setHint("jakarta.persistence.cache.retrieveMode", "BYPASS");
            query.setHint("org.hibernate.readOnly", true);
            
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
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
    public Addresses findById(Integer id) {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            return em.find(Addresses.class, id);
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
    public void insert(Addresses address) {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error inserting address", e);
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
    public void update(Addresses address) {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            em.getTransaction().begin();
            em.merge(address);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating address", e);
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
    public void delete(Integer id) {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            em.getTransaction().begin();
            Addresses address = em.find(Addresses.class, id);
            if (address != null) {
                em.remove(address);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting address", e);
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
    public void clearDefaultAddresses(Integer userId) {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            em.getTransaction().begin();
            String jpql = """
                UPDATE Addresses a
                SET a.IsDefault = false
                WHERE a.user.Id = :userId
                AND a.IsDefault = true
            """;
            em.createQuery(jpql)
                .setParameter("userId", userId)
                .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error clearing default addresses", e);
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
    public int countByUserId(Integer userId) {
        EntityManager em = null;
        try {
            em = JPAConfigs.getEntityManager();
            String jpql = "SELECT COUNT(a) FROM Addresses a WHERE a.user.Id = :userId";
            Long count = em.createQuery(jpql, Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
            return count != null ? count.intValue() : 0;
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
}

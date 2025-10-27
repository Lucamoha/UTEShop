package com.uteshop.dao.impl.web.auth;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.web.auth.IUserTokensDao;
import com.uteshop.entity.auth.UserTokens;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;

public class UserTokensDaoImpl implements IUserTokensDao {

    @Override
    public void insert(UserTokens token) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(token);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public UserTokens findByTokenAndType(String token, int type) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            TypedQuery<UserTokens> query = em.createQuery(
                    "SELECT t FROM UserTokens t " +
                            "JOIN FETCH t.user " + 
                            "WHERE t.Token = :token AND t.Type = :type " +
                            "AND t.ExpiredAt > :now AND t.UsedAt IS NULL",
                    UserTokens.class);
            query.setParameter("token", token);
            query.setParameter("type", type);
            query.setParameter("now", LocalDateTime.now());
            UserTokens result = query.getSingleResult();
            System.out.println("DAO: User loaded = " + result.getUser().getEmail()); // ← DEBUG

            return result;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void markAsUsed(Integer tokenId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            em.getTransaction().begin();
            UserTokens token = em.find(UserTokens.class, tokenId);
            if (token != null) {
                token.setUsedAt(LocalDateTime.now());
                em.merge(token);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteExpiredTokens() {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM UserTokens t WHERE t.ExpiredAt < :now")
                    .setParameter("now", LocalDateTime.now())
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteByUserId(Integer userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM UserTokens t WHERE t.user.id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}

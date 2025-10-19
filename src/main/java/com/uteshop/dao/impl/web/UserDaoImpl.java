package com.uteshop.dao.impl.web;


import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.web.IUsersDao;
import com.uteshop.entity.auth.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;


public class UserDaoImpl extends AbstractDao<Users> implements IUsersDao {

	public UserDaoImpl() {
		super(Users.class);
	}

    @Override
    public boolean checkDuplicate(String email, String phone) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            Query emailQuery = em.createQuery("SELECT u FROM Users u WHERE u.Email = :email");
            emailQuery.setParameter("email", email);
            emailQuery.setMaxResults(1);
            try {
                emailQuery.getSingleResult();
                return true;
            } catch (NoResultException e) {
                // Không tìm thấy email, tiếp tục kiểm tra phone
            }

            Query phoneQuery = em.createQuery("SELECT u FROM Users u WHERE u.Phone = :phone");
            phoneQuery.setParameter("phone", phone);
            phoneQuery.setMaxResults(1);
            try {
                phoneQuery.getSingleResult();
                return true;
            } catch (NoResultException e) {
                return false; // Không trùng lặp
            }
        } finally {
            em.close();
        }
    }

    @Override
    public Users getUserByEmail(String email) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            Query query = em.createQuery("SELECT u FROM Users u WHERE u.Email = :email", Users.class);
            query.setParameter("email", email);
            return (Users) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    @Override
    public Users getUserByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            Query query = em.createQuery("SELECT u FROM Users u WHERE u.Phone = :phone", Users.class);
            query.setParameter("phone", phone);
            return (Users) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}

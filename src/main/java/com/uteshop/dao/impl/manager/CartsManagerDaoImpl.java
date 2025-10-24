package com.uteshop.dao.impl.manager;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.manager.ICartsManagerDao;
import com.uteshop.entity.cart.Carts;
import jakarta.persistence.EntityManager;

public class CartsManagerDaoImpl implements ICartsManagerDao {

    @Override
    public Carts findCartWithItems(Integer cartId) {
        EntityManager enma = JPAConfigs.getEntityManager();

        String jpql = """
            select c from Carts c
            left join fetch c.items ci
            where c.Id = :id
        """;
        var list = enma.createQuery(jpql, Carts.class)
                .setParameter("id", cartId)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}

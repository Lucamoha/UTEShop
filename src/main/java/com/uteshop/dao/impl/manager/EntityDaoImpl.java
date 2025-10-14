package com.uteshop.dao.impl.manager;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.entity.auth.Users;
import com.uteshop.entity.branch.Branches;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.List;
import java.util.Optional;

public class EntityDaoImpl<T> extends AbstractDao<T> {
    EntityManager em = JPAConfigs.getEntityManager();

    public EntityDaoImpl(Class<T> cls) {
        super(cls);
    }

    private Attribute<? super T, ?> getValidatedSingularAttribute(String attributeName) {
        try {
            ManagedType<T> mt = em.getMetamodel().managedType(entityClass);
            Attribute<? super T, ?> attr = mt.getAttribute(attributeName);
            if (!(attr instanceof SingularAttribute<? super T,?>)) {
                throw new IllegalArgumentException("Attribute '" + attributeName + "' is not singular.");
            }
            return attr;
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown attribute '" + attributeName
                    + "' on " + entityClass.getSimpleName(), ex);
        }
    }

    public Optional<T> findByUnique(String attributeName, Object value) {
        getValidatedSingularAttribute(attributeName);
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + attributeName + " = :v";
        List<T> list = em.createQuery(jpql, entityClass).setParameter("v", value).setMaxResults(2).getResultList();
        if (list.isEmpty()) return Optional.empty();
        if (list.size() > 1) throw new NonUniqueResultException("Non-unique: " + attributeName + "=" + value);
        return Optional.of(list.get(0));
    }
}

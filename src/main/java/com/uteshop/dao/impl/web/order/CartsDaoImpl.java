package com.uteshop.dao.impl.web.order;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.web.order.ICartsDao;
import com.uteshop.entity.auth.Users;
import com.uteshop.entity.cart.CartItems;
import com.uteshop.entity.cart.Carts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.List;

public class CartsDaoImpl implements ICartsDao {

    @Override
    public Carts getOrCreateCartByUserId(Integer userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            // Tìm cart hiện có
            Query query = em.createQuery("SELECT c FROM Carts c WHERE c.user.Id = :userId", Carts.class);
            query.setParameter("userId", userId);
            try {
                return (Carts) query.getSingleResult();
            } catch (NoResultException e) {
                // Tạo cart mới nếu chưa có
                trans.begin();
                Users user = em.find(Users.class, userId);
                Carts newCart = Carts.builder()
                        .user(user)
                        .build();
                em.persist(newCart);
                trans.commit();
                return newCart;
            }
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CartItems> getCartItemsByUserId(Integer userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = """
                SELECT ci FROM CartItems ci
                JOIN FETCH ci.product p
                LEFT JOIN FETCH ci.variant v
                JOIN FETCH ci.cart c
                WHERE c.user.Id = :userId
                ORDER BY ci.Id DESC
            """;
            Query query = em.createQuery(jpql, CartItems.class);
            query.setParameter("userId", userId);
            return (List<CartItems>) query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public CartItems findCartItem(Integer cartId, Integer productId, Integer variantId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = """
                SELECT ci FROM CartItems ci
                WHERE ci.cart.Id = :cartId
                AND ci.product.Id = :productId
                AND (:variantId IS NULL AND ci.variant IS NULL OR ci.variant.Id = :variantId)
            """;
            Query query = em.createQuery(jpql, CartItems.class);
            query.setParameter("cartId", cartId);
            query.setParameter("productId", productId);
            query.setParameter("variantId", variantId);
            return (CartItems) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void addCartItem(CartItems cartItem) {
        EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(cartItem);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void updateCartItem(CartItems cartItem) {
        EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(cartItem);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void removeCartItem(Integer itemId) {
        EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            CartItems item = em.find(CartItems.class, itemId);
            if (item != null) {
                em.remove(item);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void clearCart(Integer userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "DELETE FROM CartItems ci WHERE ci.cart.user.Id = :userId";
            Query query = em.createQuery(jpql);
            query.setParameter("userId", userId);
            query.executeUpdate();
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public int getCartItemCount(Integer userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = """
                SELECT COUNT(ci) FROM CartItems ci
                WHERE ci.cart.user.Id = :userId
            """;
            Query query = em.createQuery(jpql);
            query.setParameter("userId", userId);
            Long count = (Long) query.getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public Integer findVariantByOptions(Integer productId, List<Integer> optionValueIds) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            // Query tương tự như SQL mẫu của bạn
            String jpql = """
                SELECT pv.Id FROM ProductVariants pv
                WHERE pv.product.Id = :productId
                AND (
                    SELECT COUNT(vo) FROM VariantOptions vo
                    WHERE vo.variant.Id = pv.Id
                    AND vo.optionValue.Id IN :optionValueIds
                ) = :optionCount
                AND (
                    SELECT COUNT(vo2) FROM VariantOptions vo2
                    WHERE vo2.variant.Id = pv.Id
                ) = :optionCount
            """;
            Query query = em.createQuery(jpql);
            query.setParameter("productId", productId);
            query.setParameter("optionValueIds", optionValueIds);
            query.setParameter("optionCount", (long) optionValueIds.size());
            query.setMaxResults(1);
            
            return (Integer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<java.util.Map<String, String>> removeInactiveVariantItems(Integer userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        List<java.util.Map<String, String>> removedItems = new java.util.ArrayList<>();
        
        try {
            // Tìm các cart items có variant.status = false
            String jpql = """
                SELECT ci FROM CartItems ci
                JOIN FETCH ci.product p
                JOIN FETCH ci.variant v
                JOIN FETCH ci.cart c
                WHERE c.user.Id = :userId
                AND v.Status = false
            """;
            Query query = em.createQuery(jpql, CartItems.class);
            query.setParameter("userId", userId);
            List<CartItems> inactiveItems = (List<CartItems>) query.getResultList();
            
            if (!inactiveItems.isEmpty()) {
                trans.begin();
                
                // Lưu thông tin các sản phẩm bị xóa trước khi xóa
                for (CartItems item : inactiveItems) {
                    java.util.Map<String, String> itemInfo = new java.util.HashMap<>();
                    itemInfo.put("productName", item.getProduct().getName());
                    itemInfo.put("sku", item.getVariant().getSKU());
                    removedItems.add(itemInfo);
                    
                    // Xóa cart item
                    em.remove(item);
                }
                
                trans.commit();
            }
            
            return removedItems;
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            return removedItems;
        } finally {
            em.close();
        }
    }
}

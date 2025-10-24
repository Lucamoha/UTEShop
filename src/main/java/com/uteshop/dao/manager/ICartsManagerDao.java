package com.uteshop.dao.manager;

import com.uteshop.entity.cart.Carts;

public interface ICartsManagerDao {
    Carts findCartWithItems(Integer cartId);
}

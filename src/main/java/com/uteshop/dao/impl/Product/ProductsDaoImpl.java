package com.uteshop.dao.impl.Product;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.IProductsDao;
import com.uteshop.entity.catalog.ProductImages;
import com.uteshop.entity.catalog.Products;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProductsDaoImpl extends AbstractDao<Products> implements IProductsDao {

	public ProductsDaoImpl() {
		super(Products.class);
	}

    @Override
    public List<Products> topLatestProducts() {
        EntityManager enma = JPAConfigs.getEntityManager();
        try {
            return enma.createNamedQuery("Products.findLatestProducts", Products.class)
                    .setMaxResults(12)   // tương ứng với TOP 12
                    .getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Products> findAll(int page, int pageSize) {
        EntityManager enma = JPAConfigs.getEntityManager();
        try {
            return enma.createQuery("SELECT p FROM Products p ORDER BY p.CreatedAt DESC", Products.class)
                    .setFirstResult((page - 1) * pageSize) // vị trí bắt đầu
                    .setMaxResults(pageSize)               // số lượng record
                    .getResultList();
        } finally {
            enma.close();
        }
    }

    public static void main(String[] args) {
        ProductsDaoImpl dao = new ProductsDaoImpl();
        List<Products> products = dao.findAll(2, 10);
        for (Products p : products) {
                System.out.println(p.getImages().get(0).getImageUrl());
        }
    }
}

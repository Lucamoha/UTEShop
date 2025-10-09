package com.uteshop.dao.admin;

import com.uteshop.entity.catalog.Categories;

public interface ICategoriesDao {
    Categories findBySlug(String slug);
    Categories findById(int id);
}

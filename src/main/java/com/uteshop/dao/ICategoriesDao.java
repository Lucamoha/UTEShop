package com.uteshop.dao;

import java.util.List;

import com.uteshop.entity.catalog.Categories;

public interface ICategoriesDao {
	List<Categories> findAll();
    List<Categories> findParents();
    List<Categories> findChildren(int parentId);
    Categories findBySlug(String slug);
    Categories findById(int id);
}

package com.uteshop.services.web;

import java.util.List;

import com.uteshop.entity.catalog.Categories;

public interface ICategoriesService {
	List<Categories> findAll();
    List<Categories> findParents();
    List<Categories> findChildren(int parentId);
    Categories findBySlug(String slug);
    Categories findById(int id);
    List<Categories> findAll(boolean all, int firstResult, int maxResult, String searchKeyword);
    int count(String searchKeyword);
    void insert(Categories category);
    void update(Categories category);
    void delete(int id);
}

package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.catalog.Categories;

public interface ICategoriesService {
	List<Categories> findAll();
    Categories findBySlug(String slug);
    Categories findById(int id);
    List<Categories> findAll(boolean all, int firstResult, int maxResult, String searchKeyword);
    List<Categories> findAllFetchParent(boolean all, int firstResult, int maxResult, String searchKeyword, String fetchColumnName);
    int count(String searchKeyword);
    void insert(Categories category);
    void update(Categories category);
    void delete(int id);
}

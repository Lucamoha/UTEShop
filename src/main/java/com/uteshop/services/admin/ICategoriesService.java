package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.catalog.Categories;

public interface ICategoriesService {
	List<Categories> findAll();
    Categories findBySlug(String slug);
    Categories findById(int id);
    List<Categories> findAll(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName);
    int count(String searchKeyword, String searchKeywordColumnName);
    void insert(Categories category);
    void update(Categories category);
    void delete(int id);
}

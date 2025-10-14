package com.uteshop.services.web;

import java.util.List;

import com.uteshop.entity.catalog.Categories;

public interface ICategoriesService {
	List<Categories> findAll();
    List<Categories> findParents();
    List<Categories> findChildren(int parentId);
    Categories findBySlug(String slug);
    Categories findById(int id);
}

package com.uteshop.services.Category;

import java.util.List;

import com.uteshop.entities.Categories;

public interface ICategoriesService {
	List<Categories> findAll();
    List<Categories> findParents();
    List<Categories> findChildren(int parentId);
    Categories findBySlug(String slug);
    Categories findById(int id);
}

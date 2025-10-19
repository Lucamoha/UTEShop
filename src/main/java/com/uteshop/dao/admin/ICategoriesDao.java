package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.entity.catalog.Categories;

public interface ICategoriesDao {
    Categories findBySlug(String slug);
    Categories findById(int id);
    Categories findByIdFetchColumns(Object id, List<String> columns);
    Categories findByIdFetchColumns(Object id, int firstResult, int maxResult, List<String> fetchColumnsName);
}

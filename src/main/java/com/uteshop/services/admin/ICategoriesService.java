package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.catalog.Categories;

public interface ICategoriesService {
	List<Categories> findAll();

	Categories findBySlug(String slug);

	Categories findById(int id);
	
	Categories findByIdFetchColumns(Object id, List<String> columns);
	
	Categories findByIdFetchColumns(Object id, int firstResult, int maxResult, List<String> fetchColumnsName);

	// List<Categories> findAll(boolean all, int firstResult, int maxResult, String
	// searchKeyword, String searchKeywordColumnName);
	List<Categories> findAllFetchParent(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName, String fetchColumnName);
	
	List<Categories> findAllFetchColumns(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName, List<String> fetchColumnsName);

	int count(String searchKeyword, String searchKeywordColumnName);

	void insert(Categories category);

	void update(Categories category);

	void delete(int id);
}

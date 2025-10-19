package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.catalog.Attributes;

public interface IAttributesService {
	List<Attributes> findAll();
	List<Attributes> findAll(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName);
	List<Attributes> findAllFetchColumns(List<String> fetchColumnsName);
	Attributes findByName(String name);
	Attributes findById(int id); 
	void insert(Attributes attribute);
	void update(Attributes attribute);
	void delete(int id);
	int count(String searchKeyword, String searchKeywordColumnName);
}

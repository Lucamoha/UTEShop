package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.catalog.OptionValues;

public interface IOptionValueService {
	void insert(OptionValues optionValue);
	void update(OptionValues optionValue);
	void delete(int id);
	List<OptionValues> findAll();
	List<OptionValues> findAllFetchColumns(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName, List<String> fetchColumnsName);
	OptionValues findByIdFetchColumn(int id, String fetchColumnName);
	OptionValues findById(int id);
	OptionValues findByValue(String value);
	int count(String searchKeyword, String searchKeywordColumnName);
}

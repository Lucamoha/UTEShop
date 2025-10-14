package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.catalog.OptionTypes;

public interface IOptionTypesService {
	void insert(OptionTypes optionTypes);
	void update(OptionTypes optionTypes);
	void delete(int id);
	List<OptionTypes> findAll(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName);
	OptionTypes findById(int id);
	OptionTypes findByCode(String code);
	int count(String searchKeyword, String searchKeywordColumnName);
	
}

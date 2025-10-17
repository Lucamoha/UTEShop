package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.impl.admin.OptionTypesDaoImpl;
import com.uteshop.entity.catalog.OptionTypes;
import com.uteshop.services.admin.IOptionTypesService;

public class OptionTypesServiceImpl implements IOptionTypesService {

	OptionTypesDaoImpl optionTypesDaoImpl = new OptionTypesDaoImpl();
	
	@Override
	public void insert(OptionTypes optionTypes) {
		optionTypesDaoImpl.insert(optionTypes);
	}

	@Override
	public void update(OptionTypes optionTypes) {
		optionTypesDaoImpl.update(optionTypes);
	}

	@Override
	public void delete(int id) {
		optionTypesDaoImpl.delete(id);
	}

	@Override
	public List<OptionTypes> findAll(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName) {
		return optionTypesDaoImpl.findAll(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName);
	}

	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return optionTypesDaoImpl.count(searchKeyword, searchKeywordColumnName);
	}

	@Override
	public OptionTypes findById(int id) {
		return optionTypesDaoImpl.findById(id);
	}

	@Override
	public OptionTypes findByCode(String code) {
		List<OptionTypes> listOptionType = optionTypesDaoImpl.findByColumnHasExactWord("Code", code);
		if(listOptionType != null && !listOptionType.isEmpty()) {
			return listOptionType.get(0);
		}
		return null;
	}

	@Override
	public List<OptionTypes> findAll() {
		return optionTypesDaoImpl.findAll();
	}

}

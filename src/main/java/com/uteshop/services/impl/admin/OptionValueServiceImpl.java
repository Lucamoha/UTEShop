package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.admin.IOptionValuesDao;
import com.uteshop.dao.impl.admin.OptionValuesDaoImpl;
import com.uteshop.entity.catalog.OptionValues;
import com.uteshop.services.admin.IOptionValueService;

public class OptionValueServiceImpl implements IOptionValueService {

	OptionValuesDaoImpl optionValuesDaoImpl = new OptionValuesDaoImpl();
	IOptionValuesDao optionValuesDao = new OptionValuesDaoImpl();
	
	@Override
	public OptionValues findByIdFetchColumn(int id, String fetchColumnName) {
		return optionValuesDaoImpl.findByIdFetchColumn(id, fetchColumnName);
	}

	@Override
	public void insert(OptionValues optionValue) {
		optionValuesDaoImpl.insert(optionValue);
	}

	@Override
	public void update(OptionValues optionValue) {
		optionValuesDaoImpl.update(optionValue);
	}

	@Override
	public void delete(int id) {
		optionValuesDaoImpl.delete(id);
	}

	@Override
	public List<OptionValues> findAll() {
		return optionValuesDaoImpl.findAll();
	}

	@Override
	public OptionValues findByValue(String value) {
		List<OptionValues> listOptionValue = optionValuesDaoImpl.findByColumnHasExactWord("Value", value);
		if(listOptionValue != null && !listOptionValue.isEmpty()) {
			return listOptionValue.get(0);
		}
		return null;
	}

	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return optionValuesDaoImpl.count(searchKeyword, searchKeywordColumnName);
	}

	@Override
	public List<OptionValues> findAllFetchColumns(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName, List<String> fetchColumnsName) {
		return optionValuesDaoImpl.findAllFetchColumns(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName, fetchColumnsName);
	}

	@Override
	public OptionValues findById(int id) {
		return optionValuesDaoImpl.findById(id);
	}
	
	@Override
	public List<OptionValues> findByOptionTypeId(int optionTypeId) {
		return optionValuesDao.findByOptionTypeId(optionTypeId);
	}
}

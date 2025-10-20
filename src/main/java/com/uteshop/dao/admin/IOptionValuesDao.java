package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.entity.catalog.OptionValues;

public interface IOptionValuesDao {

	List<OptionValues> findByOptionTypeId(int optionTypeId);
}

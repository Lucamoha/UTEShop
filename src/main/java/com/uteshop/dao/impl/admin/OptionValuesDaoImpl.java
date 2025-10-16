package com.uteshop.dao.impl.admin;

import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IOptionValuesDao;
import com.uteshop.entity.catalog.OptionValues;

public class OptionValuesDaoImpl extends AbstractDao<OptionValues> implements IOptionValuesDao {

	public OptionValuesDaoImpl() {
		super(OptionValues.class);
	}
}

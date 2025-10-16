package com.uteshop.services.impl.admin;

import com.uteshop.dao.impl.admin.OptionValuesDaoImpl;
import com.uteshop.entity.catalog.OptionValues;
import com.uteshop.services.admin.IOptionValueService;

public class OptionValueServiceImpl implements IOptionValueService {

	OptionValuesDaoImpl optionValuesDaoImpl = new OptionValuesDaoImpl();
	
	@Override
	public OptionValues findById(int id) {
		return optionValuesDaoImpl.findById(id);
	}

}

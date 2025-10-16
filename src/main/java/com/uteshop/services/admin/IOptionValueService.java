package com.uteshop.services.admin;

import com.uteshop.entity.catalog.OptionValues;

public interface IOptionValueService {
	OptionValues findById(int id);
}

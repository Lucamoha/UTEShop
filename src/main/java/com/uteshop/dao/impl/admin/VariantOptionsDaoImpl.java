package com.uteshop.dao.impl.admin;

import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IVariantOptionsDao;
import com.uteshop.entity.catalog.VariantOptions;

public class VariantOptionsDaoImpl extends AbstractDao<VariantOptions> implements IVariantOptionsDao {

	public VariantOptionsDaoImpl() {
		super(VariantOptions.class);
	}

}

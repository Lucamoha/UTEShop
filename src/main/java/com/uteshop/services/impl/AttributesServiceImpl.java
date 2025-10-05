package com.uteshop.services.impl;

import java.util.List;

import com.uteshop.dao.IAttributesDao;
import com.uteshop.dao.impl.AttributesDaoImpl;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.services.IAttributesService;

public class AttributesServiceImpl implements IAttributesService {

	IAttributesDao attributesDao = new AttributesDaoImpl();
	@Override
	public List<Attributes> findAll() {	
		return attributesDao.findAll();
	}

}

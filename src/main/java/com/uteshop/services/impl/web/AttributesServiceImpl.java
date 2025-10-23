package com.uteshop.services.impl.web;

import com.uteshop.dao.impl.web.AttributesDaoImpl;
import com.uteshop.dao.web.IAttributesDao;
import com.uteshop.services.web.IAttributesService;

import java.util.Map;

public class AttributesServiceImpl implements IAttributesService {

    private final IAttributesDao attributesDao;

    public AttributesServiceImpl() {
        this.attributesDao = new AttributesDaoImpl();
    }

    @Override
    public Map<String, String> getProductAttributes(int productId) {
        return attributesDao.getProductAttributes(productId);
    }
}
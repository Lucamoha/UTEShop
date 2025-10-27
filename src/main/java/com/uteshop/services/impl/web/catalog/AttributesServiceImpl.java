package com.uteshop.services.impl.web.catalog;

import com.uteshop.dao.impl.web.catalog.AttributesDaoImpl;
import com.uteshop.dao.web.catalog.IAttributesDao;
import com.uteshop.services.web.catalog.IAttributesService;

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
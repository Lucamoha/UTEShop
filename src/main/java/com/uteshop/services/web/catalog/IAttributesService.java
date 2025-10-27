package com.uteshop.services.web.catalog;

import java.util.Map;

public interface IAttributesService {
    Map<String, String> getProductAttributes(int productId);
}

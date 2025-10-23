package com.uteshop.services.web;

import java.util.Map;

public interface IAttributesService {
    Map<String, String> getProductAttributes(int productId);
}

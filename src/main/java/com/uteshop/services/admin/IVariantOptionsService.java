package com.uteshop.services.admin;

import com.uteshop.entity.catalog.VariantOptions;

public interface IVariantOptionsService {
	void insert(VariantOptions variantOption);
	void insert(Integer variantId, Integer optionValueId);
}

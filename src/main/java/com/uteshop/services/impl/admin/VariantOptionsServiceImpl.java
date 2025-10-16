package com.uteshop.services.impl.admin;

import com.uteshop.dao.impl.admin.VariantOptionsDaoImpl;
import com.uteshop.entity.catalog.OptionValues;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.VariantOptions;
import com.uteshop.services.admin.IVariantOptionsService;

public class VariantOptionsServiceImpl implements IVariantOptionsService {

	VariantOptionsDaoImpl variantOptionsDaoImpl = new VariantOptionsDaoImpl();
	@Override
	public void insert(VariantOptions variantOption) {
		variantOptionsDaoImpl.insertByMerge(variantOption);
	}
	@Override
	public void insert(Integer variantId, Integer optionValueId) {
		ProductVariants variant = new ProductVariants();
        variant.setId(variantId);

        OptionValues optionValue = new OptionValues();
        optionValue.setId(optionValueId);

        VariantOptions variantOption = new VariantOptions();
        variantOption.setId(new VariantOptions.Id(variantId, optionValueId));
        variantOption.setVariant(variant);
        variantOption.setOptionValue(optionValue);

        variantOptionsDaoImpl.insert(variantOption);
	}

}

package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.ProductAttributeValues;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.Products;

import jakarta.persistence.EntityManager;

public interface IProductsDao {
	Products findByName(String name);
	List<Object[]> getTopSellingProducts(int limit, int branchId);
	List<Attributes> findAttributesByCategoryId(Integer categoryId);
	void deleteVariantOptionsByVariantsId(int variantId, EntityManager enma);
	void deleteProductAttributeValueByProductIdAndAttributeId(int productId, int attributeId, EntityManager enma);
	ProductVariants findVariantBySKU(EntityManager enma, String sku, List<Integer> deletedIds);
	List<Integer> getAttributeIdsByCurrentCategoryOfProducts(int categoryId, EntityManager enma);
	void deleteAllProductAttributeValuesByProductId(int productId, EntityManager enma);
	void deleteProductAttributeValuesNotInNewCategoryOfProduct(int productId, List<Integer> validIds, EntityManager enma);
	ProductAttributeValues findProductAttributeValueByProductIdAndAttributeId(int productId, int attributeId, EntityManager enma);
}

package com.uteshop.services.admin;

import java.io.IOException;
import java.util.List;

import com.uteshop.dto.admin.ProductAttributeDisplayModel;
import com.uteshop.dto.admin.ProductVariantDisplayModel;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.ProductAttributeValues;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.Products;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IProductsService {
	List<Products> findAll();

	Products findById(int id);

	Products findByIdFetchColumns(Object id, List<String> fetchColumnsName);

	void insert(Products product);

	void update(Products products);

	void delete(int id);

	List<Products> findAll(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName);

	int count(String searchKeyword, String searchKeywordColumnName);

	Products findByName(String name);

	List<Object[]> getTopSellingProducts(int limit, int branchId);

	List<Attributes> findAttributesByCategoryId(Integer categoryId);

	void saveOrUpdateProductWithTransaction(Products product, boolean isUpdate, HttpServletRequest req,
			String uploadPath) throws Exception;

	void handleDeletedVariants(HttpServletRequest req, EntityManager enma);

	void handleDeletedAttributes(Products product, HttpServletRequest req, EntityManager enma);

	List<String> handleExistingVariants(HttpServletRequest req, EntityManager enma);

	void handleNewVariants(Products product, HttpServletRequest req, EntityManager enma, List<String> releasedSkus);

	ProductVariants findVariantBySKU(EntityManager enma, String sku, List<Integer> deletedIds);

	void handleExistingAttributes(Products product, HttpServletRequest req, EntityManager enma);

	void handleNewAttributes(Products product, HttpServletRequest req, EntityManager enma);

	void handleImages(Products product, HttpServletRequest req, String uploadPath, EntityManager enma)
			throws IOException;

	String buildProductFolderPath(Products product);

	String sanitizeForPath(String text);

	String generateProductImageFileName(Products product, String originalFileName);

	void handleImagesForNewProduct(Products product, HttpServletRequest req, String uploadPath, EntityManager enma)
			throws IOException;

	void deleteAttributesNotInCategory(Products product, EntityManager enma);

	ProductAttributeValues findProductAttributeValue(int productId, int attributeId, EntityManager enma);

	void handleLoadAttributes(HttpServletRequest req, HttpServletResponse resp) throws IOException;

	List<ProductVariantDisplayModel> buildTempVariantList(HttpServletRequest req);

	List<ProductAttributeDisplayModel> buildTempAttributeList(HttpServletRequest req);

	String getSourceUploadPath(HttpServletRequest req);

	void handleLoadOptionValues(HttpServletRequest req, HttpServletResponse resp) throws IOException;

	void prepareFormDataForError(HttpServletRequest req, Products product, Categories category, boolean isUpdate);

}

package com.uteshop.dao.admin;

import com.uteshop.entity.catalog.Attributes;
import java.util.List;

public interface IAttributesDao {
	List<Attributes> findAll();
	List<Attributes> findAllFetchColumns(List<String> fetchColumnsName);
	boolean existsInCategoryAttributes(int attributeId);
	boolean existsInProductAttributeValues(int attributeId);
}

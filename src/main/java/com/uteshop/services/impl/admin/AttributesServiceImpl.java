package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.admin.IAttributesDao;
import com.uteshop.dao.impl.admin.AttributesDaoImpl;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.services.admin.IAttributesService;

public class AttributesServiceImpl implements IAttributesService {

	IAttributesDao attributesDao = new AttributesDaoImpl();
	AttributesDaoImpl attributesDaoImpl = new AttributesDaoImpl();
	@Override
	public List<Attributes> findAll() {	
		return attributesDao.findAll();
	}
	@Override
	public Attributes findByName(String name) {
		List<Attributes> attributes = attributesDaoImpl.findByColumnContainingWord("Name", name);
		if(!attributes.isEmpty()) {
			return attributes.get(0);
		}
		return null;
	}
	@Override
	public void insert(Attributes attribute) {	
		attributesDaoImpl.insert(attribute);
	}
	@Override
	public Attributes findById(int id) {
		return attributesDaoImpl.findById(id);
	}
	@Override
	public List<Attributes> findAll(boolean all, int firstResult, int maxResult, String searchKeyword,
			String searchKeywordColumnName) {
		return attributesDaoImpl.findAll(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName);
	}
	@Override
	public void update(Attributes attribute) {
		attributesDaoImpl.update(attribute);
	}
	@Override
	public void delete(int id) {
		attributesDaoImpl.delete(attributesDao);
	}
	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return attributesDaoImpl.count(searchKeyword, searchKeywordColumnName);
	}

}

package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.catalog.Attributes;

public interface IAttributesService {
	List<Attributes> findAll();
	Attributes findByName(String name);
	Attributes findById(int id); 
	void insert(Attributes attribute);
}

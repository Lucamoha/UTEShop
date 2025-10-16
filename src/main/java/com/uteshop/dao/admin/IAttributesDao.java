package com.uteshop.dao.admin;

import com.uteshop.entity.catalog.Attributes;
import java.util.List;

public interface IAttributesDao {
	List<Attributes> findAll();
}

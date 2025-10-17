package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.entity.branch.Branches;

public interface IBranchesService {
	void insert(Branches branch);
	void update(Branches branch);
	void delete(int id);
	List<Branches> findAll();
	List<Branches> findAllFetch(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName, String fetchColumn);
	Branches findByIdFetchColumn(int id, String column);
	Branches findByName(String name);
	int count(String searchKeyword, String searchKeywordColumnName);
	
}

package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.impl.admin.BranchesDaoImpl;
import com.uteshop.entity.branch.Branches;
import com.uteshop.services.admin.IBranchesService;

public class BranchesServiceImpl implements IBranchesService {

	BranchesDaoImpl branchesDaoImpl = new BranchesDaoImpl();
	
	@Override
	public void insert(Branches branch) {
		branchesDaoImpl.insert(branch);
	}

	@Override
	public void update(Branches branch) {
		branchesDaoImpl.update(branch);
	}

	@Override
	public void delete(int id) {
		branchesDaoImpl.delete(id);
	}

	@Override
	public List<Branches> findAll(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName) {
		return branchesDaoImpl.findAll(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName);
	}

	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return branchesDaoImpl.count(searchKeyword, searchKeywordColumnName);
	}

	@Override
	public Branches findById(int id) {
		return branchesDaoImpl.findById(id);
	}

	@Override
	public Branches findByName(String name) {
		List<Branches> listBranch = branchesDaoImpl.findByColumnHasExactWord("Name", name);
		if(listBranch != null && !listBranch.isEmpty()) {
			return listBranch.get(0);
		}
		return null;
	}

	@Override
	public List<Branches> findAll() {
		return branchesDaoImpl.findAll();
	}

}

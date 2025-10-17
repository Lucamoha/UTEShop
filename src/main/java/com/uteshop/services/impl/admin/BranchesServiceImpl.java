package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.impl.admin.BranchesDaoImpl;
import com.uteshop.entity.branch.Branches;
import com.uteshop.entity.catalog.Attributes;
import com.uteshop.services.admin.IBranchesService;

import jakarta.persistence.EntityNotFoundException;

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
		Branches branch = branchesDaoImpl.findById(id);
	    if (branch == null) {
	        throw new EntityNotFoundException("Không tìm thấy chi nhánh");
	    }

	    boolean hasInventory = branchesDaoImpl.existsInInventory(id);

	    if (hasInventory) {
	        throw new IllegalStateException("Không thể xóa chi nhánh có hàng tồn kho đang hoạt động");
	    }
		
		branchesDaoImpl.delete(id);
	}

	@Override
	public List<Branches> findAllFetch(boolean all, int firstResult, int maxResult, String searchKeyword, String searchKeywordColumnName, String fetchColumn) {
		return branchesDaoImpl.findAllFetchParent(all, firstResult, maxResult, searchKeyword, searchKeywordColumnName, fetchColumn);
	}

	@Override
	public int count(String searchKeyword, String searchKeywordColumnName) {
		return branchesDaoImpl.count(searchKeyword, searchKeywordColumnName);
	}

	@Override
	public Branches findByIdFetchColumn(int id, String column) {	    
	    return branchesDaoImpl.findByIdFetchColumn(id, column);
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

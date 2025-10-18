package com.uteshop.services.impl.admin;

import java.util.ArrayList;
import java.util.List;

import com.uteshop.dao.admin.IProductVariantsDao;
import com.uteshop.dao.impl.admin.BranchInventoryDaoImpl;
import com.uteshop.dao.impl.admin.BranchesDaoImpl;
import com.uteshop.dao.impl.admin.ProductVariantsDaoImpl;
import com.uteshop.entity.branch.BranchInventory;
import com.uteshop.entity.branch.Branches;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.services.admin.IBranchesService;

import jakarta.persistence.EntityNotFoundException;

public class BranchesServiceImpl implements IBranchesService {

	BranchesDaoImpl branchesDaoImpl = new BranchesDaoImpl();
	BranchInventoryDaoImpl branchInventoryDaoImpl = new BranchInventoryDaoImpl();
	IProductVariantsDao productVariantsDaoImpl = new ProductVariantsDaoImpl();
	
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

	@Override
	public List<BranchInventory> findInventoryByBranchId(Integer branchId) {
		return branchesDaoImpl.findInventoryByBranchId(branchId);
	}

	@Override
	public List<BranchInventory> findOrCreateInventoriesByBranchId(Integer branchId) {
	   return branchesDaoImpl.findOrCreateInventoriesByBranchId(branchId);
	}

	@Override
	public void update(BranchInventory branchInventory) {
		branchInventoryDaoImpl.update(branchInventory);
	}

	@Override
	public List<BranchInventory> createEmptyInventoriesForAllVariants() {
		return branchesDaoImpl.createEmptyInventoriesForAllVariants();
	}

	@Override
	public List<BranchInventory> findInventoriesWithOptionsByBranchId(Integer branchId) {
		List<ProductVariants> variants = productVariantsDaoImpl.findAll();
		List<BranchInventory> inventories = new ArrayList<>();
	    for (ProductVariants variant : variants) {
	        BranchInventory bi = new BranchInventory();

	        BranchInventory.Id id = new BranchInventory.Id();
	        id.setBranchId(branchId);
	        id.setVariantId(variant.getId());
	        bi.setId(id);

	        bi.setBranch(branchesDaoImpl.findById(branchId));
	        bi.setVariant(variant);
	        bi.setBranchStock(0);

	        inventories.add(bi);
	    }
	    return inventories;
	}

	@Override
	public Long countInventory(int branchId) {
		return branchesDaoImpl.countInventory(branchId);
	}

}

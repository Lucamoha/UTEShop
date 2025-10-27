package com.uteshop.services.impl.web.branch;

import com.uteshop.dao.impl.web.branch.BranchesDaoImpl;
import com.uteshop.dao.web.branch.IBranchesDao;
import com.uteshop.entity.branch.Branches;
import com.uteshop.services.web.branch.IBranchesService;

import java.util.List;
import java.util.Map;

public class BranchesServiceImpl implements IBranchesService {
    
    private final IBranchesDao branchesDao = new BranchesDaoImpl();
    
    @Override
    public List<Branches> getAllActiveBranches() {
        return branchesDao.findAllActiveBranches();
    }
    
    @Override
    public Branches getBranchById(Integer id) {
        return branchesDao.findById(id);
    }
    
    @Override
    public Integer getBranchStock(Integer branchId, Integer variantId) {
        if (branchId == null || variantId == null) {
            return 0;
        }
        return branchesDao.getBranchStock(branchId, variantId);
    }
    
    @Override
    public Map<Integer, Integer> getBranchStockForVariants(Integer branchId, List<Integer> variantIds) {
        // Use bulk query to prevent multiple DB calls and deadlock
        return branchesDao.getBranchStockBulk(branchId, variantIds);
    }
}

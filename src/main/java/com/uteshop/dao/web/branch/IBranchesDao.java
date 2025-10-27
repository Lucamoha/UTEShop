package com.uteshop.dao.web.branch;

import com.uteshop.entity.branch.Branches;
import java.util.List;
import java.util.Map;

public interface IBranchesDao {
    List<Branches> findAllActiveBranches();
    Branches findById(Integer id);
    Integer getBranchStock(Integer branchId, Integer variantId);
    Map<Integer, Integer> getBranchStockBulk(Integer branchId, List<Integer> variantIds);
}

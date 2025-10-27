package com.uteshop.services.web.branch;

import com.uteshop.entity.branch.Branches;
import java.util.List;
import java.util.Map;

public interface IBranchesService {
    List<Branches> getAllActiveBranches();
    Branches getBranchById(Integer id);
    Integer getBranchStock(Integer branchId, Integer variantId);
    Map<Integer, Integer> getBranchStockForVariants(Integer branchId, List<Integer> variantIds);
}

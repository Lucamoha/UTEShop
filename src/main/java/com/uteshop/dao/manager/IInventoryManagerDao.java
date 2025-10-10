package com.uteshop.dao.manager;

import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dto.manager.inventory.InventoryRow;
import com.uteshop.entity.branch.BranchInventory;

import java.util.Map;

public interface IInventoryManagerDao {
    PageResult<InventoryRow> search(Integer branchId,
                                    Integer categoryId,
                                    String keyword,
                                    int page, int size,
                                    String sort, String dir);

    Map<String, Integer> bulkAdjustBySku(Integer branchId, Map<String, Integer> deltaBySku);
}

package com.uteshop.services.manager;

import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dto.manager.inventory.InventoryRow;

import java.util.List;
import java.util.Map;

public interface IInventoryManagerService {
    PageResult<InventoryRow> search(Integer branchId,
                                    Integer categoryId,
                                    String keyword,
                                    int page, int size,
                                    String sort, String dir);

    Map<String, Integer> bulkAdjustBySku(Integer branchId, Map<String, Integer> deltaBySku);

    List<InventoryRow> findAllForExport(Integer branchId);
}

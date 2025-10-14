package com.uteshop.services.impl.manager;

import com.uteshop.dao.manager.IInventoryManagerDao;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dao.impl.manager.InventoryManagerDaoImpl;
import com.uteshop.dto.manager.inventory.InventoryRow;
import com.uteshop.services.manager.IInventoryManagerService;

import java.util.List;
import java.util.Map;

public class InventoryManagerServiceImpl implements IInventoryManagerService {
    IInventoryManagerDao dao = new InventoryManagerDaoImpl();

    @Override
    public PageResult<InventoryRow> search(Integer branchId, Integer categoryId, String keyword, int page, int size, String sort, String dir) {
        return dao.search(branchId, categoryId, keyword, page, size, sort, dir);
    }

    @Override
    public Map<String, Integer> bulkAdjustBySku(Integer branchId, Map<String, Integer> deltaBySku) {
        return dao.bulkAdjustBySku(branchId, deltaBySku);
    }

    @Override
    public List<InventoryRow> findAllForExport(Integer branchId) {
        return dao.findAllForExport(branchId);
    }
}

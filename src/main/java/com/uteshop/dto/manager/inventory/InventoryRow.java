package com.uteshop.dto.manager.inventory;

public record InventoryRow(Integer productId, String productName,
                           Integer variantId, String sku,
                           Integer qty, java.math.BigDecimal price) {
}

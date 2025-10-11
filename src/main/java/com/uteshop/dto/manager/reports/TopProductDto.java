package com.uteshop.dto.manager.reports;

import java.math.BigDecimal;

public record TopProductDto(Integer productId, String name, long qty, BigDecimal amount) {
}

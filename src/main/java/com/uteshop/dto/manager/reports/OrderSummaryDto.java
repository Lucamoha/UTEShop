package com.uteshop.dto.manager.reports;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryDto(Integer id, String customer, String phone,
                              String status, String paymentStatus,
                              BigDecimal total, LocalDateTime createdAt) {
}

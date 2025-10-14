package com.uteshop.dto.manager.reports;

public record PaymentBreakdownDTO(String method, long orders, double share) {
}

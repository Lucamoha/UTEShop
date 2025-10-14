package com.uteshop.dto.manager.reports;

import java.math.BigDecimal;

public record RevenueSeriesResponse(String bucket, String from, String to,
                                    BigDecimal total, java.util.List<RevenuePoint> points) {
}

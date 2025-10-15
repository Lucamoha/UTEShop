package com.uteshop.dto.manager.reports;

import java.util.List;

public record OrderStatsDTO(long totalOrders,

                            long delivered, double deliveredRatio,
                            long canceled, double canceledRatio,
                            long returned, double returnedRatio,

                            List<PaymentBreakdownDTO> paymentBreakdown) {
}

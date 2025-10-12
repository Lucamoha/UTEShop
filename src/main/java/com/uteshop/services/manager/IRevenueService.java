package com.uteshop.services.manager;

import com.uteshop.dto.manager.reports.RevenueSeriesResponse;

import java.time.LocalDate;

public interface IRevenueService {
    public RevenueSeriesResponse buildSeries(Integer branchId,
                                             LocalDate from, LocalDate to,
                                             String bucket);
}

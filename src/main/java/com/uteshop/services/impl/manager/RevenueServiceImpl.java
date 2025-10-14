package com.uteshop.services.impl.manager;

import com.uteshop.dao.manager.IOrdersManagerDao;
import com.uteshop.dao.impl.manager.OrdersManagerDaoImpl;
import com.uteshop.dto.manager.reports.RevenuePoint;
import com.uteshop.dto.manager.reports.RevenueSeriesResponse;
import com.uteshop.services.manager.IRevenueService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class RevenueServiceImpl implements IRevenueService {
    IOrdersManagerDao ordersDao = new OrdersManagerDaoImpl();

    @Override
    public RevenueSeriesResponse buildSeries(Integer branchId,
                                             LocalDate from, LocalDate to,
                                             String bucket) {
        List<Object[]> daily = ordersDao.revenueDaily(branchId, from, to);

        Map<LocalDate, BigDecimal> byDate = new HashMap<>();
        daily.forEach(r -> {
            LocalDate d = ((java.sql.Date) r[0]).toLocalDate();
            BigDecimal amt = (BigDecimal) r[1];
            byDate.put(d, amt);
        });

        List<RevenuePoint> points;
        switch (bucket) {
            case "week" -> points = groupByWeek(from, to, byDate);
            case "month" -> points = groupByMonth(from, to, byDate);
            default -> points = fillByDay(from, to, byDate); // "day"
        }

        BigDecimal total = points.stream()
                .map(RevenuePoint::y)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RevenueSeriesResponse(bucket, from.toString(), to.toString(), total, points);
    }

    private List<RevenuePoint> fillByDay(LocalDate from, LocalDate to, Map<LocalDate, BigDecimal> byDate) {
        List<RevenuePoint> res = new ArrayList<>();
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            res.add(new RevenuePoint(d.toString(), byDate.getOrDefault(d, BigDecimal.ZERO)));
        }
        return res;
    }

    private List<RevenuePoint> groupByWeek(LocalDate from, LocalDate to, Map<LocalDate, BigDecimal> byDate) {
        WeekFields wf = WeekFields.ISO;
        Map<String, BigDecimal> sum = new LinkedHashMap<>();
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            String key = d.get(wf.weekBasedYear()) + "-W" + String.format("%02d", d.get(wf.weekOfWeekBasedYear()));
            sum.merge(key, byDate.getOrDefault(d, BigDecimal.ZERO), BigDecimal::add);
        }
        return sum.entrySet().stream()
                .map(e -> new RevenuePoint(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private List<RevenuePoint> groupByMonth(LocalDate from, LocalDate to, Map<LocalDate, BigDecimal> byDate) {
        Map<YearMonth, BigDecimal> sum = new LinkedHashMap<>();
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            YearMonth ym = YearMonth.from(d);
            sum.merge(ym, byDate.getOrDefault(d, BigDecimal.ZERO), BigDecimal::add);
        }
        return sum.entrySet().stream()
                .map(e -> new RevenuePoint(e.getKey().toString(), e.getValue()))
                .collect(Collectors.toList());
    }
}

package com.uteshop.api.manager.inventory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteshop.dto.manager.inventory.InventoryRow;
import com.uteshop.services.impl.manager.InventoryManagerServiceImpl;
import com.uteshop.services.manager.IInventoryManagerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@WebServlet(urlPatterns = "/api/manager/inventory/export-excel")
public class InventoryExportExcelApi extends HttpServlet {

    private static final IInventoryManagerService iInventoryManagerService = new InventoryManagerServiceImpl();

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Integer branchId(HttpServletRequest req){
        return (Integer) req.getSession().getAttribute("branchId");
    }

    private static void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        MAPPER.writeValue(resp.getOutputStream(), body);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer branchId = branchId(req);
        if (branchId == null) {
            writeJson(resp, 401, Map.of("ok", false, "error", "Unauthenticated/No branch"));
            return;
        }

        List<InventoryRow> rows = iInventoryManagerService.findAllForExport(branchId);

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Inventory");

            CreationHelper ch = wb.getCreationHelper();

            // Styles
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont(); headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            CellStyle moneyStyle = wb.createCellStyle();
            moneyStyle.setDataFormat(ch.createDataFormat().getFormat("#,##0.00"));

            CellStyle intStyle = wb.createCellStyle();
            intStyle.setDataFormat(ch.createDataFormat().getFormat("0"));

            // Header
            int r = 0;
            Row h = sheet.createRow(r++);
            String[] headers = {"ProductId","ProductName","SKU","Price","Qty"};
            for (int i=0;i<headers.length;i++) {
                var cell = h.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Body
            for (InventoryRow row : rows) {
                Row rr = sheet.createRow(r++);
                int c = 0;

                // ProductId: nếu có nguy cơ dài, ghi dạng text
                rr.createCell(c++).setCellValue(row.productId() == null ? "" : String.valueOf(row.productId()));
                rr.createCell(c++).setCellValue(Objects.toString(row.productName(), ""));
                rr.createCell(c++).setCellValue(Objects.toString(row.sku(), ""));

                var priceCell = rr.createCell(c++);
                double price = row.price() == null ? 0D : row.price().doubleValue();
                priceCell.setCellValue(price);
                priceCell.setCellStyle(moneyStyle);

                var qtyCell = rr.createCell(c);
                int qty = row.qty() == null ? 0 : row.qty();
                qtyCell.setCellValue(qty);
                qtyCell.setCellStyle(intStyle);
            }

            for (int i=0;i<headers.length;i++) sheet.autoSizeColumn(i);

            String fname = "inventory-" + DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")
                    .format(LocalDateTime.now()) + ".xlsx";
            resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + fname + "\"");
            wb.write(resp.getOutputStream());
            resp.flushBuffer();
        }
    }
}

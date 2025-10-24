package com.uteshop.controller.manager;

import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.entity.branch.Branches;
import com.uteshop.entity.order.OrderItems;
import com.uteshop.entity.order.Orders;
import com.uteshop.enums.OrderEnums;
import com.uteshop.enums.PaymentEnums;
import com.uteshop.services.impl.manager.OrdersManagerServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.BaseFont;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@WebServlet(urlPatterns = {"/manager/invoice"})
public class InvoiceController extends HttpServlet {
    EntityDaoImpl<Branches> branchesDao = new EntityDaoImpl<>(Branches.class);
    OrdersManagerServiceImpl ordersManagerService = new OrdersManagerServiceImpl();

    private Integer branchId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("branchId");
    }

    private static Font font(BaseFont bf, float size, int style) {
        return new Font(bf, size, style);
    }

    private static PdfPCell cell(String text, Font f, int hAlign, int vAlign, Color bg, float padding) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setHorizontalAlignment(hAlign);
        c.setVerticalAlignment(vAlign);
        c.setPadding(padding);
        if (bg != null) c.setBackgroundColor(bg);
        return c;
    }

    private static String nullSafe(Object v) {
        return v == null ? "-" : String.valueOf(v);
    }

    private static PdfPCell noBorder(PdfPCell c) {
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private static String vnd(BigDecimal n) {
        if (n == null) n = BigDecimal.ZERO;
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return nf.format(n);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer bId = branchId(req);
        if (bId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String sid = req.getParameter("id");
        Integer id;
        try { id = Integer.valueOf(sid); }
        catch (Exception e) { resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai định dạng id"); return; }

        Orders o = ordersManagerService.findByIdWithItems(id, bId);
        if (o == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng #" + id); return; }
        Branches branches = branchesDao.findById(bId);

        String fileName = ("invoice_" + o.getId() + ".pdf");
        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encoded);

        try {
            Document doc = new Document(PageSize.A4, 36, 36, 48, 36);
            PdfWriter writer = PdfWriter.getInstance(doc, resp.getOutputStream());
            doc.open();

            // Load font Việt
            String fontPath = getServletContext().getRealPath("/templates/manager/fonts/NotoSans-Regular.ttf");
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font H1 = font(bf, 16, Font.BOLD);
            Font H2 = font(bf, 12, Font.BOLD);
            Font P  = font(bf, 10.5f, Font.NORMAL);
            Font S  = font(bf, 9, Font.NORMAL);

            // Header hóa đơn
            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG", H1);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(6);
            doc.add(title);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            String created = o.getCreatedAt() != null ? o.getCreatedAt().format(fmt) : "-";

            PdfPTable head = new PdfPTable(new float[]{60, 40});
            head.setWidthPercentage(100);

            String left =   "UTEShop\n" +
                    "Chi nhánh: " + branches.getName() + "\n" +
                    "Địa chỉ: " + branches.getAddress() + "\n" +
                    "Hotline: " + branches.getPhone();

            String right =  "Mã đơn: #" + o.getId() + "\n" +
                    "Ngày tạo: " + created + "\n" +
                    "PT thanh toán: " + (o.getPayment()!=null ? PaymentEnums.Method.label(o.getPayment().getMethod()) : "-") + "\n" +
                    "Trạng thái TT: " + OrderEnums.PaymentStatus.label(o.getPaymentStatus());

            PdfPCell l = cell(left, P, Element.ALIGN_LEFT, Element.ALIGN_TOP, null, 6);
            PdfPCell r = cell(right, P, Element.ALIGN_LEFT, Element.ALIGN_TOP, null, 6);

            r.setHorizontalAlignment(Element.ALIGN_RIGHT);
            head.addCell(l);
            head.addCell(r);
            head.setSpacingAfter(8);
            doc.add(head);

            // Thông tin người nhận
            Paragraph infoTitle = new Paragraph("Thông tin người nhận", H2);
            infoTitle.setSpacingBefore(4);
            infoTitle.setSpacingAfter(4);
            doc.add(infoTitle);

            String addr = (o.getAddressLine()!=null?o.getAddressLine():"");
            if (o.getWard()!=null && !o.getWard().isBlank()) addr += ", " + o.getWard();
            if (o.getDistrict()!=null && !o.getDistrict().isBlank()) addr += ", " + o.getDistrict();
            if (o.getCity()!=null && !o.getCity().isBlank()) addr += ", " + o.getCity();

            Paragraph recipient = new Paragraph(
                    "Họ tên: " + nullSafe(o.getReceiverName()) + "\n" +
                            "SĐT: "    + nullSafe(o.getPhone()) + "\n" +
                            "Địa chỉ: " + addr + (o.getNote()!=null && !o.getNote().isBlank()? ("\nGhi chú: " + o.getNote()) : ""),
                    P
            );
            recipient.setSpacingAfter(8);
            doc.add(recipient);

            // Bảng sản phẩm
            PdfPTable tbl = new PdfPTable(new float[]{6, 34, 18, 12, 14, 16}); // #, SP, Biến thể, SL, Đơn giá, Thành tiền
            tbl.setWidthPercentage(100);

            Color headBg = new Color(245, 245, 245);
            tbl.addCell(cell("#",        P, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headBg, 6));
            tbl.addCell(cell("Sản phẩm", P, Element.ALIGN_LEFT,   Element.ALIGN_MIDDLE, headBg, 6));
            tbl.addCell(cell("Biến thể", P, Element.ALIGN_LEFT,   Element.ALIGN_MIDDLE, headBg, 6));
            tbl.addCell(cell("SL",       P, Element.ALIGN_RIGHT,  Element.ALIGN_MIDDLE, headBg, 6));
            tbl.addCell(cell("Đơn giá",  P, Element.ALIGN_RIGHT,  Element.ALIGN_MIDDLE, headBg, 6));
            tbl.addCell(cell("Thành tiền", P, Element.ALIGN_RIGHT,Element.ALIGN_MIDDLE, headBg, 6));

            List<OrderItems> items = o.getItems();
            int idx = 1;
            if (items != null) {
                for (OrderItems it : items) {
                    String product = it.getProduct()!=null ? nullSafe(it.getProduct().getName()) : "-";
                    String variant = (it.getVariant()!=null ? nullSafe(it.getVariant().getSKU()) : "-");
                    BigDecimal price = it.getPrice()!=null ? it.getPrice() : BigDecimal.ZERO;
                    int qty = it.getQuantity();
                    BigDecimal line = price.multiply(BigDecimal.valueOf(qty));

                    tbl.addCell(cell(String.valueOf(idx++), P, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 6));
                    tbl.addCell(cell(product, P, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, 6));
                    tbl.addCell(cell(variant, P, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, 6));
                    tbl.addCell(cell(String.valueOf(qty), P, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 6));
                    tbl.addCell(cell(vnd(price), P, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 6));
                    tbl.addCell(cell(vnd(line),  P, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 6));
                }
            }
            doc.add(tbl);

            // Tổng tiền
            Paragraph spacer = new Paragraph(" ", P);
            spacer.setSpacingBefore(6);
            doc.add(spacer);

            PdfPTable totals = new PdfPTable(new float[]{60, 40});
            totals.setWidthPercentage(100);

            PdfPTable rightTotals = new PdfPTable(new float[]{58, 42});
            rightTotals.setWidthPercentage(100);

            rightTotals.addCell(noBorder(cell("Tạm tính", P, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 6)));
            rightTotals.addCell(noBorder(cell(vnd(o.getSubtotal()), P, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 6)));

            rightTotals.addCell(noBorder(cell("Giảm giá", P, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 6)));
            rightTotals.addCell(noBorder(cell(vnd(o.getDiscountAmount()), P, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 6)));

            rightTotals.addCell(noBorder(cell("Phí vận chuyển", P, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 6)));
            rightTotals.addCell(noBorder(cell(vnd(o.getShippingFee()), P, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 6)));

            Font totalFont = font(bf, 12, Font.BOLD);
            rightTotals.addCell(noBorder(cell("TỔNG THANH TOÁN", totalFont, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 8)));
            rightTotals.addCell(noBorder(cell(vnd(o.getTotalAmount()), totalFont, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, null, 8)));

            totals.addCell(noBorder(new PdfPCell())); // cột trống bên trái
            PdfPCell eright = new PdfPCell(rightTotals);
            eright.setBorder(Rectangle.NO_BORDER);
            totals.addCell(eright);
            doc.add(totals);

            Paragraph note = new Paragraph("Cảm ơn Quý khách đã mua sắm tại UTEShop!", S);
            note.setSpacingBefore(12);
            doc.add(note);

            doc.close();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            resp.reset();
            resp.sendError(500, "Lỗi tạo PDF: " + e.getMessage());
        }
    }
}

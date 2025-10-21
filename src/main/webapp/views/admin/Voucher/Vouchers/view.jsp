<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="row">
    <div class="col-md-8 offset-md-2 mt-4">
        <div class="card shadow-lg">
            <div class="card-header bg-dark text-white">
                <h4 class="mb-0">Chi Tiết Voucher</h4>
            </div>
            <div class="card-body bg-light">

                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Mã Số</label>
                            <input type="text" class="form-control" value="${voucher.Id}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Mã</label>
                            <input type="text" class="form-control" value="${voucher.Code}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Mô Tả</label>
                            <input type="text" class="form-control" value="${voucher.DescText}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Loại</label>
                            <input type="text" class="form-control"
                                   value="${voucher.Type == 1 ? 'Giảm Giá Phần Trăm' : (voucher.Type == 2 ? 'Giảm Giá Cố Định' : 'Không xác định')}"
                                   readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Giá Trị</label>
                            <input type="text" class="form-control"
                                   value="<c:choose><c:when test='${voucher.Type == 1}'><fmt:formatNumber value='${voucher.Value}' maxFractionDigits='2'/>%</c:when><c:when test='${voucher.Type == 2}'><fmt:formatNumber value='${voucher.Value}' type='currency' currencySymbol='₫'/></c:when><c:otherwise>${voucher.Value} (Loại không xác định)</c:otherwise></c:choose>"
                                   readonly/>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Số Lần Sử Dụng Tối Đa</label>
                            <input type="text" class="form-control" value="${voucher.MaxUses}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Số Lần Đã Sử Dụng</label>
                            <input type="text" class="form-control" value="${voucher.TotalUsed}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Ngày Bắt Đầu</label>
                            <input type="text" class="form-control" value="<fmt:formatDate value='${voucher.StartsAt}' pattern='dd/MM/yyyy HH:mm'/>" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Ngày Kết Thúc</label>
                            <input type="text" class="form-control" value="<fmt:formatDate value='${voucher.EndsAt}' pattern='dd/MM/yyyy HH:mm'/>" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Hoạt Động</label>
                            <input type="text" class="form-control" value="${voucher.IsActive ? 'Có' : 'Không'}" readonly/>
                        </div>
                    </div>
                </div>

                <div class="mt-4">
                    <a href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/edit?id=${voucher.Id}"
                       class="btn btn-primary btn-lg"
                       style="background-color:#0d47a1; border-color:#0d47a1;">
                        Sửa
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/list"
                       class="btn btn-success btn-lg"
                       style="background-color:#1b5e20; border-color:#1b5e20;">
                        Quay Lại
                    </a>
                </div>

            </div>
        </div>
    </div>
</section>
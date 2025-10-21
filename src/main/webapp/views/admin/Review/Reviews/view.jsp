<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<%
    java.time.format.DateTimeFormatter fullFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    pageContext.setAttribute("fullFormatter", fullFormatter);
%>

<section class="row">
    <div class="col-md-10 offset-md-1 mt-4">
        <div class="card shadow-lg border-0 rounded-4">
            <div class="card-header bg-gradient text-white d-flex align-items-center justify-content-between p-3 rounded-top-4" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                <div class="d-flex align-items-center">
                    <i class="fa fa-star fa-2x me-3 text-warning"></i>
                    <h3 class="mb-0 fw-bold">Chi Tiết Đánh Giá</h3>
                </div>
                <span class="badge bg-light text-dark fs-6">Đánh Giá #${review.id}</span>
            </div>
            <div class="card-body p-4 bg-gradient" style="background: linear-gradient(to bottom, #f8f9fa, #e9ecef);">
                <div class="row g-4">
                    <div class="col-md-6">
                        <div class="info-card h-100 p-3 border rounded-3 bg-white shadow-sm">
                            <h6 class="text-muted mb-3"><i class="fa fa-user me-2"></i>Thông Tin Người Dùng</h6>
                            <div class="mb-3">
                                <label class="fw-semibold text-dark">Mã Người Dùng</label>
                                <p class="fs-5 fw-medium">#${review.user.id}</p>
                            </div>
                            <div class="mb-3">
                                <label class="fw-semibold text-dark">Tên Người Dùng</label>
                                <p class="fs-5 text-primary">${review.user.fullName}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="info-card h-100 p-3 border rounded-3 bg-white shadow-sm">
                            <h6 class="text-muted mb-3"><i class="fa fa-box me-2"></i>Thông Tin Sản Phẩm</h6>
                            <div class="mb-3">
                                <label class="fw-semibold text-dark">Mã Sản Phẩm</label>
                                <p class="fs-5 fw-medium">#${review.product.id}</p>
                            </div>
                            <div class="mb-3">
                                <label class="fw-semibold text-dark">Tên Sản Phẩm</label>
                                <p class="fs-5 text-success">${review.product.name}</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row g-4 mt-3">
                    <div class="col-md-12">
                        <div class="info-card p-3 border rounded-3 bg-white shadow-sm">
                            <h6 class="text-muted mb-3"><i class="fa fa-star me-2"></i>Chi Tiết Đánh Giá</h6>
                            <div class="row">
                                <div class="col-md-3 mb-3">
                                    <label class="fw-semibold text-dark">Đánh Giá</label>
                                    <p class="fs-5 fw-medium text-warning">${review.rating}/5</p>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <label class="fw-semibold text-dark">Có Media</label>
                                    <p class="fs-5 fw-medium">${review.hasMedia ? 'Có' : 'Không'}</p>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <label class="fw-semibold text-dark">Xác Minh Mua Hàng</label>
                                    <p class="fs-5 fw-medium">${review.purchaseVerified ? 'Có' : 'Không'}</p>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <label class="fw-semibold text-dark">Trạng Thái</label>
                                    <p class="fs-5 fw-medium ${review.status ? 'text-success' : 'text-danger'}">${review.status ? 'Kích Hoạt' : 'Không Kích Hoạt'}</p>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="fw-semibold text-dark">Nội Dung</label>
                                <p class="fs-5 text-dark">${review.content}</p>
                            </div>
                            <div class="mb-3">
                                <label class="fw-semibold text-dark">Ngày Tạo</label>
                                <p class="fs-5 text-muted">${review.createdAt.format(fullFormatter)}</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="d-flex justify-content-between mt-4 pt-3 border-top">
                    <a href="${pageContext.request.contextPath}/admin/Review/Reviews/edit?id=${review.id}"
                       class="btn btn-primary btn-lg px-4 py-2 rounded-pill shadow-sm"
                       style="background: linear-gradient(135deg, #0d47a1 0%, #1976d2 100%); border: none;">
                        <i class="fa fa-edit me-2"></i>Sửa Đánh Giá
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/Review/Reviews/list"
                       class="btn btn-success btn-lg px-4 py-2 rounded-pill shadow-sm"
                       style="background: linear-gradient(135deg, #1b5e20 0%, #388e3c 100%); border: none;">
                        <i class="fa fa-arrow-left me-2"></i>Quay Lại Danh Sách
                    </a>
                </div>
            </div>
        </div>
    </div>
</section>
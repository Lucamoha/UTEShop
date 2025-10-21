<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
    <div class="col-md-10 offset-md-1 mt-4">
        <div class="card shadow-lg border-0 rounded-4">
            <div class="card-header bg-gradient text-white d-flex align-items-center p-3 rounded-top-4" style="background: linear-gradient(135deg, #ffc107 0%, #ff8f00 100%);">
                <i class="fa fa-edit fa-2x me-3"></i>
                <h3 class="mb-0 fw-bold">Sửa Đánh Giá</h3>
            </div>
            <div class="card-body p-4 bg-gradient" style="background: linear-gradient(to bottom, #f8f9fa, #e9ecef);">
                <form action="${pageContext.request.contextPath}/admin/Review/Reviews/edit" method="post">
                    <input type="hidden" name="id" value="${review.id}" />
                    <div class="row g-4">
                        <div class="col-md-6">
                            <div class="input-group-wrapper p-3 border rounded-3 bg-white shadow-sm">
                                <label class="form-label fw-semibold text-dark mb-2">Người Dùng Hiện Tại</label>
                                <input type="text" class="form-control border-0 shadow-none" value="${review.user.fullName} (ID: ${review.user.id})" readonly />
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="input-group-wrapper p-3 border rounded-3 bg-white shadow-sm">
                                <label class="form-label fw-semibold text-dark mb-2">Người Dùng Mới</label>
                                <select name="userId" class="form-select border-0 shadow-none" required>
                                    <option value="${review.user.id}">${review.user.fullName} (ID: ${review.user.id})</option>
                                    <c:forEach items="${users}" var="user">
                                        <option value="${user.id}">${user.fullName} (ID: ${user.id})</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row g-4 mt-3">
                        <div class="col-md-6">
                            <div class="input-group-wrapper p-3 border rounded-3 bg-white shadow-sm">
                                <label class="form-label fw-semibold text-dark mb-2">Sản Phẩm Hiện Tại</label>
                                <input type="text" class="form-control border-0 shadow-none" value="${review.product.name} (ID: ${review.product.id})" readonly />
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="input-group-wrapper p-3 border rounded-3 bg-white shadow-sm">
                                <label class="form-label fw-semibold text-dark mb-2">Sản Phẩm Mới</label>
                                <select name="productId" class="form-select border-0 shadow-none" required>
                                    <option value="${review.product.id}">${review.product.name} (ID: ${review.product.id})</option>
                                    <c:forEach items="${products}" var="product">
                                        <option value="${product.id}">${product.name} (ID: ${product.id})</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row g-4 mt-3">
                        <div class="col-md-6">
                            <div class="input-group-wrapper p-3 border rounded-3 bg-white shadow-sm">
                                <label class="form-label fw-semibold text-dark mb-2">Đánh Giá (1-5)</label>
                                <select name="rating" class="form-select border-0 shadow-none" required>
                                    <c:forEach var="i" begin="1" end="5">
                                        <option value="${i}" ${review.rating == i ? 'selected' : ''}>${i} Sao</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="input-group-wrapper p-3 border rounded-3 bg-white shadow-sm">
                                <label class="form-label fw-semibold text-dark mb-2">Trạng Thái</label>
                                <div class="form-check mt-2">
                                    <input class="form-check-input" type="checkbox" name="status" id="status" value="true" ${review.status ? 'checked' : ''} />
                                    <label class="form-check-label" for="status">Kích Hoạt</label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row g-4 mt-3">
                        <div class="col-md-6">
                            <div class="input-group-wrapper p-3 border rounded-3 bg-white shadow-sm">
                                <label class="form-label fw-semibold text-dark mb-2">Có Media</label>
                                <div class="form-check mt-2">
                                    <input class="form-check-input" type="checkbox" name="hasMedia" id="hasMedia" value="true" ${review.hasMedia ? 'checked' : ''} />
                                    <label class="form-check-label" for="hasMedia">Có</label>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="input-group-wrapper p-3 border rounded-3 bg-white shadow-sm">
                                <label class="form-label fw-semibold text-dark mb-2">Xác Minh Mua Hàng</label>
                                <div class="form-check mt-2">
                                    <input class="form-check-input" type="checkbox" name="purchaseVerified" id="purchaseVerified" value="true" ${review.purchaseVerified ? 'checked' : ''} />
                                    <label class="form-check-label" for="purchaseVerified">Có</label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row g-4 mt-3">
                        <div class="col-md-12">
                            <div class="input-group-wrapper p-3 border rounded-3 bg-white shadow-sm">
                                <label class="form-label fw-semibold text-dark mb-2">Nội Dung</label>
                                <textarea name="content" class="form-control border-0 shadow-none" rows="4" required>${review.content}</textarea>
                            </div>
                        </div>
                    </div>
                    <div class="d-flex justify-content-between mt-4 pt-3 border-top">
                        <button type="submit" class="btn btn-warning btn-lg px-4 py-2 rounded-pill shadow-sm"
                                style="background: linear-gradient(135deg, #ffc107 0%, #ff8f00 100%); border: none; color: #212529;">
                            <i class="fa fa-update me-2"></i>Cập Nhật Đánh Giá
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/Review/Reviews/list"
                           class="btn btn-secondary btn-lg px-4 py-2 rounded-pill shadow-sm">
                            <i class="fa fa-times me-2"></i>Hủy
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Danh Sách Đánh Giá</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2>Danh Sách Đánh Giá</h2>
    <div class="mb-3 d-flex justify-content-between">
        <a href="${pageContext.request.contextPath}/admin/Review/Reviews/export" class="btn btn-success">Xuất Excel</a>
        <form action="${pageContext.request.contextPath}/admin/Review/Reviews/filter" method="get" class="d-flex">
            <select name="rating" class="form-select me-2" style="width: 150px;">
                <option value="">Tất cả</option>
                <c:forEach var="i" begin="1" end="5">
                    <option value="${i}" ${selectedRating == i ? 'selected' : ''}>${i} Sao</option>
                </c:forEach>
            </select>
            <button type="submit" class="btn btn-primary">Lọc</button>
        </form>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">Thao tác thành công!</div>
    </c:if>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>Mã</th>
            <th>Người Dùng</th>
            <th>Sản Phẩm</th>
            <th>Đánh Giá</th>
            <th>Nội Dung</th>
            <th>Trạng Thái</th>
            <th>Ngày Tạo</th>
            <th>Hành Động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="review" items="${listReviews}">
            <tr>
                <td>${review.id}</td>
                <td>
                    <c:choose>
                        <c:when test="${review.user != null}">
                            ${review.user.id} -
                            <c:choose>
                                <c:when test="${review.user.fullName != null}">${review.user.fullName}</c:when>
                                <c:otherwise>Không xác định</c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>Không có</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${review.product != null}">
                            ${review.product.id} -
                            <c:choose>
                                <c:when test="${review.product.name != null}">${review.product.name}</c:when>
                                <c:otherwise>Không xác định</c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>Không có</c:otherwise>
                    </c:choose>
                </td>
                <td>${review.rating} Sao</td>
                <td>${review.content}</td>
                <td>${review.status ? 'Kích Hoạt' : 'Không Kích Hoạt'}</td>
                <td>${review.createdAt}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/Review/Reviews/view?id=${review.id}" class="btn btn-info btn-sm">Xem</a>
                    <a href="${pageContext.request.contextPath}/admin/Review/Reviews/edit?id=${review.id}" class="btn btn-warning btn-sm">Sửa</a>
                    <a href="${pageContext.request.contextPath}/admin/Review/Reviews/delete?id=${review.id}" class="btn btn-danger btn-sm" onclick="return confirm('Xóa?')">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
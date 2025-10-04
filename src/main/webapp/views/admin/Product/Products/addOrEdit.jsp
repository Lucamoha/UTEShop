<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<section class="row">
    <div class="col-6 offset-3 mt-4">
        <form action="${pageContext.request.contextPath}/admin/Product/Products/saveOrUpdate" method="POST">
            <div class="card">
                <div class="card-header">
                    <h2>
                        <c:choose>
                            <c:when test="${not empty product.id}">Edit Product</c:when>
                            <c:otherwise>Add Product</c:otherwise>
                        </c:choose>
                    </h2>
                </div>
                <div class="card-body">

                    <!-- Hidden Product ID (nếu đang edit) -->
                    <input type="hidden" name="id" value="${product.id}" />

                    <!-- Product Name -->
                    <div class="mb-3">
                        <label class="form-label">Product Name:</label>
                        <input type="text" name="name" class="form-control"
                               value="${product.name}" placeholder="Enter product name" />
                    </div>

                    <!-- Slug -->
                    <div class="mb-3">
                        <label class="form-label">Slug:</label>
                        <input type="text" name="slug" class="form-control"
                               value="${product.slug}" placeholder="Unique slug" />
                    </div>

                    <!-- Description -->
                    <div class="mb-3">
                        <label class="form-label">Description:</label>
                        <textarea name="description" class="form-control" rows="3">${product.description}</textarea>
                    </div>

                    <!-- Base Price -->
                    <div class="mb-3">
                        <label class="form-label">Base Price:</label>
                        <input type="number" step="0.01" name="basePrice" class="form-control"
                               value="${product.basePrice}" placeholder="Enter price" />
                    </div>
                   

                    <!-- Category Select -->
                    <div class="mb-3">
                        <label class="form-label">Category:</label>
                        <select name="categoryId" class="form-select">
                            <option value="">-- Select Category --</option>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.id}"
                                    <c:if test="${product.category.id == cat.id}">selected</c:if>>
                                    ${cat.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Status -->
                    <div class="mb-3">
                        <label class="form-label">Status:</label>
                        <select name="status" class="form-select">
                            <option value="1" <c:if test="${product.status == true}">selected</c:if>>Active</option>
                            <option value="0" <c:if test="${product.status == false}">selected</c:if>>Inactive</option>
                        </select>
                    </div>

                    <!-- Submit -->
                    <div class="mt-3">
                        <button type="submit" class="btn btn-primary">Save</button>
                        <a href="${pageContext.request.contextPath}/admin/Product/Products/list" class="btn btn-secondary">Cancel</a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</section>

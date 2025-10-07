<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<section class="row">
    <div class="col-8 offset-2 mt-4">
        <div class="card">
            <div class="card-header">
                <h4>Product Detail</h4>
            </div>
            <div class="card-body">
                <table class="table table-bordered">
                    <tr>
                        <th>ID</th>
                        <td>${product.id}</td>
                    </tr>
                    <tr>
                        <th>Name</th>
                        <td>${product.name}</td>
                    </tr>
                    <tr>
                        <th>Slug</th>
                        <td>${product.slug}</td>
                    </tr>
                    <tr>
                        <th>Description</th>
                        <td>${product.description}</td>
                    </tr>
                    <tr>
                        <th>Base Price</th>
                        <td>${product.basePrice}</td>
                    </tr>
                    <tr>
                        <th>Status</th>
                        <td>
                            <c:choose>
                                <c:when test="${product.status}">Active</c:when>
                                <c:otherwise>Inactive</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Category</th>
                        <td>
                            <c:if test="${not empty product.category}">
                                ${product.category.name}
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th>Created At</th>
                        <td>${product.createdAt}</td>
                    </tr>
                    <tr>
                        <th>Updated At</th>
                        <td>${product.updatedAt}</td>
                    </tr>
                </table>
            </div>
            <div class="card-footer text-right">
                <!-- Back -->
                <a href="${pageContext.request.contextPath}/admin/Product/Products/searchpaginated"
                   class="btn btn-secondary">Back</a>
                
                <!-- Edit -->
                <a href="${pageContext.request.contextPath}/admin/Product/Products/edit/${product.id}" 
                   class="btn btn-warning">Edit</a>
                
                <!-- Delete -->
                <a href="${pageContext.request.contextPath}/admin/Product/Products/delete/${product.id}" 
                   class="btn btn-danger"
                   onclick="return confirm('Are you sure you want to delete this product?')">
                   Delete
                </a>
            </div>
        </div>
    </div>
</section>

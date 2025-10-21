<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
    <div class="col-md-6 offset-md-3 mt-4">
        <div class="card shadow-lg">
            <div class="card-header bg-dark text-white">
                <h4 class="mb-0">Edit User</h4>
            </div>
            <div class="card-body bg-light">
                <form action="${pageContext.request.contextPath}/admin/User/Users/edit" method="post">
                    <!-- ID ẩn để update -->
                    <input type="hidden" name="id" value="${user.id}"/>

                    <div class="mb-3">
                        <label class="form-label">Full Name</label>
                        <input type="text" name="fullName" class="form-control" 
                               value="${user.fullName}" required/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-control" 
                               value="${user.email}" required/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Phone</label>
                        <input type="text" name="phone" class="form-control" 
                               value="${user.phone}"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Password</label>
                        <input type="password" name="password" class="form-control" 
                               placeholder="Enter new password if you want to change"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">User Role</label>
                        <select name="userRole" class="form-select" required>
                            <option value="USER" ${user.userRole == 'USER' ? 'selected' : ''}>USER</option>
                            <option value="ADMIN" ${user.userRole == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                            <option value="MANAGER" ${user.userRole == 'MANAGER' ? 'selected' : ''}>MANAGER</option>
                        </select>
                    </div>

                    <!-- Nút Save xanh dương đậm -->
                    <button type="submit" class="btn btn-primary btn-lg" 
                            style="background-color:#0d47a1; border-color:#0d47a1;">
                        Update
                    </button>
                    <!-- Nút Cancel xanh lá đậm -->
                    <a href="${pageContext.request.contextPath}/admin/User/Users/list" 
                       class="btn btn-success btn-lg" 
                       style="background-color:#1b5e20; border-color:#1b5e20;">
                        Cancel
                    </a>
                </form>
            </div>
        </div>
    </div>
</section>

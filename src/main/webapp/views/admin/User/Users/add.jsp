<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<div class="col-md-6 offset-md-3 mt-4">
		<div class="card shadow-lg">
			<div class="card-header bg-dark text-white">
				<h4 class="mb-0">Add User</h4>
			</div>
			<div class="card-body bg-light">
				<form
					action="${pageContext.request.contextPath}/admin/User/Users/add"
					method="post">
					<input type="hidden" name="action" value="add" />

					<div class="mb-3">
						<label class="form-label">Full Name</label> <input type="text"
							name="fullName" class="form-control" required />
					</div>

					<div class="mb-3">
						<label class="form-label">Email</label> <input type="email"
							name="email" class="form-control" required />
					</div>

					<div class="mb-3">
						<label class="form-label">Phone</label> <input type="text"
							name="phone" class="form-control" />
					</div>

					<div class="mb-3">
						<label class="form-label">Password</label> <input type="password"
							name="password" class="form-control" required />
					</div>

					<div class="mb-3">
						<label class="form-label">User Role</label> <select
							name="userRole" class="form-select" required>
							<option value="USER">USER</option>
							<option value="ADMIN">ADMIN</option>
							<option value="MANAGER">MANAGER</option>
						</select>
					</div>

					<!-- Save xanh dương đậm -->
					<button type="submit" class="btn btn-primary btn-lg"
						style="background-color: #0d47a1; border-color: #0d47a1;">
						Save</button>

					<!-- Cancel xanh lá đậm -->
					<a href="${pageContext.request.contextPath}/admin/User/Users/list"
						class="btn btn-success btn-lg"
						style="background-color: #1b5e20; border-color: #1b5e20;">
						Cancel </a>
				</form>
			</div>
		</div>
	</div>
</section>

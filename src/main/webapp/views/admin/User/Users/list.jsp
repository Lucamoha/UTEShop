<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<div class="col mt-4">
		<div class="card">
			<div class="card-header">USER LIST</div>
			<div class="card-body">

				<!-- Hiển thị thông báo -->
				<c:if test="${message != null}">
					<div class="alert alert-primary" role="alert">
						<i>${message}</i>
					</div>
				</c:if>

				<div class="table-responsive">
					<a href="${pageContext.request.contextPath}/admin/User/Users/add"
						class="btn btn-dark mb-2">Add User</a>

					<table class="table table-striped">
						<thead>
							<tr>
								<!-- <th>Id</th> -->
								<th>Full Name</th>
								<th>Email</th>
								<th>Phone</th>
								<th>User Role</th>
								<!-- <th>Created At</th>
								<th>Update dAt</th> -->
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${listUser}" var="item">
								<tr>
									<%-- <td>${item.id}</td> --%>
									<td>${item.fullName}</td>
									<td>${item.email}</td>
									<td>${item.phone}</td>
									<td>${item.userRole}</td>
									<%-- <td>${item.createdAt}</td>
									<td>${item.updatedAt}</td> --%>
									<td><a
										href="${pageContext.request.contextPath}/admin/User/Users/view?id=${item.id}"
										class="btn btn-outline-info"><i class="fa fa-info"></i></a> <a
										href="${pageContext.request.contextPath}/admin/User/Users/edit?id=${item.id}"
										class="btn btn-outline-warning"><i class="fa fa-edit"></i></a>
										<a
										href="${pageContext.request.contextPath}/admin/User/Users/delete?id=${item.id}"
										class="btn btn-outline-danger"><i class="fa fa-trash"></i></a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

			</div>
		</div>
	</div>
</section>
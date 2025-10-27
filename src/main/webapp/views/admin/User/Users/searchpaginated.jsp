<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<div class="col mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Danh Sách Người Dùng</h4>
			</div>
			<div class="card-body">

				<c:if test="${message != null}">
					<div class="alert alert-primary" role="alert">
						<i>${message}</i>
					</div>
				</c:if>

				<div class="table-responsive">
					<table class="table table-striped align-middle text-center">
						<thead class="table-dark">
							<tr>
								<th>Họ Tên</th>
								<th>Email</th>
								<th>Trạng Thái</th>
								<th>Vai Trò</th>
								<th>Hành Động</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${listUser}" var="item">
								<tr>
									<td>${item.fullName}</td>
									<td>${item.email}</td>
									<td><c:choose>
											<c:when test="${item.isActive}">
												Kích Hoạt
                                        	</c:when>
											<c:otherwise>
												Không Kích Hoạt
                                        	</c:otherwise>
										</c:choose></td>
									<td>${item.userRole}</td>
									<td><a
										href="${pageContext.request.contextPath}/admin/User/Users/view?id=${item.id}"
										class="btn btn-outline-info" title="Xem"><i class="fa fa-info"></i></a> <a
										href="${pageContext.request.contextPath}/admin/User/Users/edit?id=${item.id}"
										class="btn btn-outline-warning" title="Sửa"><i class="fa fa-edit"></i></a>
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
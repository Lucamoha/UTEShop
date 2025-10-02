<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<div class="col mt-4">
		<div class="card">
			<div class="card-header">List Category</div>
			<div class="card-body">

				<!-- Hiển thị thông báo -->
				<c:if test="${message != null}">
					<div class="alert alert-primary" role="alert">
						<i>${message}</i>
					</div>
				</c:if>

				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<!-- <th>Id</th> -->
								<th>Category Name</th>
								<th>Name</th>
								<th>Slug</th>
								<!-- <th>Description</th> -->
								<th>Base Price</th>
								<th>Status</th>
								<!-- <th>Created At</th>
								<th>Update dAt</th> -->
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${listSys}" var="item">
								<tr>
									<%-- <td>${item.id}</td> --%>
									<td>${item.category.name}</td>
									<td>${item.name}</td>
									<td>${item.slug}</td>
									<%-- <td>${item.description}</td> --%>
									<td>${item.basePrice}</td>
									<td>${item.status}</td>
									<%-- <td>${item.createdAt}</td>
									<td>${item.updatedAt}</td> --%>
									<td><a
										href="${pageContext.request.contextPath}/admin/category/view/id=${cate.categoryId}"
										class="btn btn-outline-info"><i class="fa fa-info"></i></a> <a
										href="${pageContext.request.contextPath}/admin/category/edit/id=${cate.categoryId}"
										class="btn btn-outline-warning"><i class="fa fa-edit"></i></a>
										<a
										href="${pageContext.request.contextPath}/admin/category/delete/id=${cate.categoryId}"
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
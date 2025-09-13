<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<table border="1" cellpadding="5" cellspacing="0">
	<thead>
		<tr>
			<th>STT</th>
			<th>Id</th>
			<th>Category Name</th>
			<th>Name</th>
			<th>Slug</th>
			<th>Description</th>
			<th>BasePrice</th>
			<th>Status</th>
			<th>CreatedAt</th>
			<th>UpdatedAt</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${listSys}" var="item" varStatus="STT">
			<tr>
				<td>${STT.index + 1}</td>
				<td>${item.id}</td>
				<td>${item.category.name}</td>
				<td>${item.name}</td>
				<td>${item.slug}</td>
				<td>${item.description}</td>
				<td>${item.basePrice}</td>
				<td>${item.status}</td>
				<td>${item.createdAt}</td>
				<td>${item.updatedAt}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
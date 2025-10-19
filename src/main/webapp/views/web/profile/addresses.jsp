<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/templates/css/profile.css">

<!-- Page title / breadcrumb -->
<div class="container p-t-60 p-b-30">
	<div class="row">
		<div class="col-12">
			<h1></h1>
		</div>
	</div>
</div>

<div class="profile-container bg0 p-t-60 p-b-60">
    <div class="profile-layout">
        <!-- Sidebar -->
        <aside class="profile-sidebar">
            <div class="profile-user-info">
                <div class="profile-avatar">
                    ${sessionScope.user.fullName.substring(0, 1).toUpperCase()}
                </div>
                <div class="profile-user-name">${sessionScope.user.fullName}</div>
            </div>
            
            <ul class="profile-menu">
                <li class="profile-menu-item">
                    <a href="${pageContext.request.contextPath}/profile" class="profile-menu-link">
                        <svg class="profile-menu-icon" fill="currentColor" viewBox="0 0 20 20">
                            <path d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"/>
                        </svg>
                        <span>Thông Tin Cá Nhân</span>
                    </a>
                </li>
                <li class="profile-menu-item">
                    <a href="${pageContext.request.contextPath}/profile/addresses" class="profile-menu-link active">
                        <svg class="profile-menu-icon" fill="currentColor" viewBox="0 0 20 20">
                            <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"/>
                        </svg>
                        <span>Địa Chỉ Của Tôi</span>
                    </a>
                </li>
                <li class="profile-menu-item">
                    <a href="${pageContext.request.contextPath}/profile/change-password" class="profile-menu-link">
                        <svg class="profile-menu-icon" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M18 8a6 6 0 01-7.743 5.743L10 14l-1 1-1 1H6v2H2v-4l4.257-4.257A6 6 0 1118 8zm-6-4a1 1 0 100 2 2 2 0 012 2 1 1 0 102 0 4 4 0 00-4-4z" clip-rule="evenodd"/>
                        </svg>
                        <span>Đổi Mật Khẩu</span>
                    </a>
                </li>
                <li class="profile-menu-item">
                    <a href="${pageContext.request.contextPath}/orders" class="profile-menu-link">
                        <svg class="profile-menu-icon" fill="currentColor" viewBox="0 0 20 20">
                            <path d="M3 1a1 1 0 000 2h1.22l.305 1.222a.997.997 0 00.01.042l1.358 5.43-.893.892C3.74 11.846 4.632 14 6.414 14H15a1 1 0 000-2H6.414l1-1H14a1 1 0 00.894-.553l3-6A1 1 0 0017 3H6.28l-.31-1.243A1 1 0 005 1H3zM16 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM6.5 18a1.5 1.5 0 100-3 1.5 1.5 0 000 3z"/>
                        </svg>
                        <span>Đơn Hàng Của Tôi</span>
                    </a>
                </li>
                <li class="profile-menu-item">
                    <a href="${pageContext.request.contextPath}/logout" class="profile-menu-link logout-link">
                        <svg class="profile-menu-icon" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 001 1h12a1 1 0 001-1V4a1 1 0 00-1-1H3zm11 4.414l-4.293 4.293a1 1 0 01-1.414 0L4 7.414 5.414 6l3.293 3.293L13.586 6 15 7.414z" clip-rule="evenodd"/>
                        </svg>
                        <span>Đăng Xuất</span>
                    </a>
                </li>
            </ul>
        </aside>
        
        <!-- Main Content -->
        <main class="profile-content">
            <div class="profile-header">
                <h1 class="profile-title">Địa Chỉ Của Tôi</h1>
                <button class="btn btn-primary" onclick="openAddModal()">+ Thêm Địa Chỉ Mới</button>
            </div>
            
            <c:if test="${param.success == 'added'}">
                <div class="alert alert-success">Thêm địa chỉ mới thành công!</div>
            </c:if>
            <c:if test="${param.success == 'updated'}">
                <div class="alert alert-success">Cập nhật địa chỉ thành công!</div>
            </c:if>
            <c:if test="${param.success == 'deleted'}">
                <div class="alert alert-success">Xóa địa chỉ thành công!</div>
            </c:if>
            <c:if test="${param.success == 'default_set'}">
                <div class="alert alert-success">Đã thiết lập địa chỉ mặc định!</div>
            </c:if>
            <c:if test="${param.error == 'duplicate_address'}">
                <div class="alert alert-error">Địa chỉ này đã tồn tại!</div>
            </c:if>
            <c:if test="${param.error == 'invalid_phone'}">
                <div class="alert alert-error">Số điện thoại không hợp lệ! (phải có 10-11 chữ số)</div>
            </c:if>
            <c:if test="${param.error == 'missing_fields'}">
                <div class="alert alert-error">Vui lòng điền đầy đủ các trường bắt buộc!</div>
            </c:if>
            <c:if test="${param.error == 'not_found'}">
                <div class="alert alert-error">Không tìm thấy địa chỉ!</div>
            </c:if>
            <c:if test="${param.error == 'system_error'}">
                <div class="alert alert-error">Có lỗi hệ thống xảy ra, vui lòng thử lại!</div>
            </c:if>
            
            <div class="address-list">
                <c:choose>
                    <c:when test="${empty addresses}">
                        <div class="address-empty">
                            <div class="address-empty-icon">📍</div>
                            <p>Bạn chưa có địa chỉ nào</p>
                            <p style="font-size: 14px; color: #aaa; margin-top: 8px;">Thêm địa chỉ để nhận hàng nhanh hơn</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${addresses}" var="addr">
                            <div class="address-card ${addr.isDefault ? 'default' : ''}">
                                <div class="address-header">
                                    <div>
                                        <div class="address-name">${addr.fullName}</div>
                                        <c:if test="${not empty addr.label}">
                                            <span style="font-size: 13px; color: #888;">(${addr.label})</span>
                                        </c:if>
                                    </div>
                                    <div class="address-badges">
                                        <c:if test="${addr.isDefault}">
                                            <span class="badge badge-default">Mặc Định</span>
                                        </c:if>
                                    </div>
                                </div>
                                
                                <div class="address-details">
                                    <div class="address-phone">Điện thoại: ${addr.phone}</div>
                                    <div style="margin-top: 8px;">
                                        ${addr.addressLine}<br>
                                        <c:if test="${not empty addr.ward}">${addr.ward}, </c:if>
                                        <c:if test="${not empty addr.district}">${addr.district}, </c:if>
                                        ${addr.city}
                                    </div>
                                </div>
                                
                                <div class="address-actions">
                                    <button class="btn btn-outline btn-sm edit-address-btn" 
                                            data-id="${addr.id}"
                                            data-label="${addr.label}"
                                            data-fullname="${addr.fullName}"
                                            data-phone="${addr.phone}"
                                            data-address="${addr.addressLine}"
                                            data-ward="${addr.ward}"
                                            data-district="${addr.district}"
                                            data-city="${addr.city}"
                                            data-default="${addr.isDefault}">
                                        Chỉnh Sửa
                                    </button>
                                    <c:if test="${!addr.isDefault}">
                                        <form method="post" action="${pageContext.request.contextPath}/profile/addresses/set-default" style="display: inline;">
                                            <input type="hidden" name="id" value="${addr.id}">
                                            <button type="submit" class="btn btn-secondary btn-sm">Đặt Làm Mặc Định</button>
                                        </form>
                                        <button type="button" class="btn btn-secondary btn-sm delete-address-btn" 
                                                data-id="${addr.id}">Xóa</button>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
</div>

<!-- Add Address Modal -->
<div id="addModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2 class="modal-title">Thêm Địa Chỉ Mới</h2>
            <button class="modal-close" onclick="closeAddModal()">&times;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/profile/addresses/add" id="addAddressForm" novalidate>
            <div class="modal-body">
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label form-label-required">Họ và Tên</label>
                        <input type="text" name="fullName" class="form-input" required placeholder="Nguyễn Văn A">
                    </div>
                    <div class="form-group">
                        <label class="form-label form-label-required">Số Điện Thoại</label>
                        <input type="tel" name="phone" class="form-input" required placeholder="0901234567">
                    </div>
                </div>
                
                <div class="form-group">
                    <label class="form-label">Loại địa chỉ (Tùy chọn)</label>
                    <input type="text" name="label" class="form-input" placeholder="VD: Nhà, Văn phòng">
                </div>
                
                <div class="form-group">
                    <label class="form-label form-label-required">Địa Chỉ</label>
                    <input type="text" name="addressLine" class="form-input" required placeholder="Số nhà, tên đường">
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Phường/Xã</label>
                        <input type="text" name="ward" class="form-input" placeholder="Phường 1">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Quận/Huyện</label>
                        <input type="text" name="district" class="form-input" placeholder="Quận 1">
                    </div>
                </div>
                
                <div class="form-group">
                    <label class="form-label form-label-required">Tỉnh/Thành Phố</label>
                    <input type="text" name="city" class="form-input" required placeholder="TP. Hồ Chí Minh">
                </div>
                
                <div class="form-group">
                    <label class="form-checkbox">
                        <input type="checkbox" name="isDefault">
                        <span>Đặt làm địa chỉ mặc định</span>
                    </label>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeAddModal()">Hủy</button>
                <button type="submit" class="btn btn-primary">Thêm Địa Chỉ</button>
            </div>
        </form>
    </div>
</div>

<!-- Edit Address Modal -->
<div id="editModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2 class="modal-title">Chỉnh Sửa Địa Chỉ</h2>
            <button class="modal-close" onclick="closeEditModal()">&times;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/profile/addresses/update" id="editForm" novalidate>
            <input type="hidden" name="id" id="editId">
            <div class="modal-body">
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label form-label-required">Họ và Tên</label>
                        <input type="text" name="fullName" id="editFullName" class="form-input" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label form-label-required">Số Điện Thoại</label>
                        <input type="tel" name="phone" id="editPhone" class="form-input" required>
                    </div>
                </div>
                
                <div class="form-group">
                    <label class="form-label">Loại địa chỉ (Tùy chọn)</label>
                    <input type="text" name="label" id="editLabel" class="form-input">
                </div>
                
                <div class="form-group">
                    <label class="form-label form-label-required">Địa Chỉ</label>
                    <input type="text" name="addressLine" id="editAddressLine" class="form-input" required>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Phường/Xã</label>
                        <input type="text" name="ward" id="editWard" class="form-input">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Quận/Huyện</label>
                        <input type="text" name="district" id="editDistrict" class="form-input">
                    </div>
                </div>
                
                <div class="form-group">
                    <label class="form-label form-label-required">Tỉnh/Thành Phố</label>
                    <input type="text" name="city" id="editCity" class="form-input" required>
                </div>
                
                <div class="form-group">
                    <label class="form-checkbox">
                        <input type="checkbox" name="isDefault" id="editIsDefault">
                        <span>Đặt làm địa chỉ mặc định</span>
                    </label>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeEditModal()">Hủy</button>
                <button type="submit" class="btn btn-primary">Cập Nhật</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openAddModal() {
        document.getElementById('addModal').classList.add('active');
    }
    
    function closeAddModal() {
        document.getElementById('addModal').classList.remove('active');
        // Reset form
        const form = document.getElementById('addAddressForm');
        form.reset();
        // Reset validation
        form.querySelectorAll('.form-input').forEach(input => {
            input.style.borderColor = '';
        });
    }
    
    function closeEditModal() {
        document.getElementById('editModal').classList.remove('active');
        // Reset validation
        const form = document.getElementById('editForm');
        form.querySelectorAll('.form-input').forEach(input => {
            input.style.borderColor = '';
        });
    }
    
    // Validate form before submit
    function validateForm(form) {
        let isValid = true;
        const requiredFields = form.querySelectorAll('.form-input[required]');
        
        // Check empty required fields
        requiredFields.forEach(field => {
            if (!field.value.trim()) {
                field.style.borderColor = '#dc3545';
                isValid = false;
            } else {
                field.style.borderColor = '';
            }
        });
        
        // Validate phone format
        const phoneInput = form.querySelector('input[name="phone"]');
        if (phoneInput && phoneInput.value.trim()) {
            const phonePattern = /^[0-9]{10,11}$/;
            if (!phonePattern.test(phoneInput.value.trim())) {
                phoneInput.style.borderColor = '#dc3545';
                isValid = false;
                swal({
                    title: "Số điện thoại không hợp lệ",
                    text: "Số điện thoại phải có 10-11 chữ số",
                    icon: "error",
                    button: "OK"
                });
            }
        }
        
        return isValid;
    }
    
    // Handle edit button clicks using data attributes
    document.addEventListener('DOMContentLoaded', function() {
        // Add form validation
        const addForm = document.getElementById('addAddressForm');
        addForm.addEventListener('submit', function(e) {
            if (!validateForm(this)) {
                e.preventDefault();
            }
        });
        
        // Edit form validation
        const editForm = document.getElementById('editForm');
        editForm.addEventListener('submit', function(e) {
            if (!validateForm(this)) {
                e.preventDefault();
            }
        });
        
        // Remove red border on input
        document.querySelectorAll('.form-input').forEach(input => {
            input.addEventListener('input', function() {
                if (this.value.trim()) {
                    this.style.borderColor = '';
                }
            });
        });
        
        const editButtons = document.querySelectorAll('.edit-address-btn');
        editButtons.forEach(function(btn) {
            btn.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                const label = this.getAttribute('data-label');
                const fullName = this.getAttribute('data-fullname');
                const phone = this.getAttribute('data-phone');
                const address = this.getAttribute('data-address');
                const ward = this.getAttribute('data-ward');
                const district = this.getAttribute('data-district');
                const city = this.getAttribute('data-city');
                const isDefault = this.getAttribute('data-default') === 'true';
                
                document.getElementById('editId').value = id;
                document.getElementById('editLabel').value = label || '';
                document.getElementById('editFullName').value = fullName;
                document.getElementById('editPhone').value = phone;
                document.getElementById('editAddressLine').value = address;
                document.getElementById('editWard').value = ward || '';
                document.getElementById('editDistrict').value = district || '';
                document.getElementById('editCity').value = city;
                document.getElementById('editIsDefault').checked = isDefault;
                document.getElementById('editModal').classList.add('active');
            });
        });
    });
    
    // Close modal when clicking outside
    window.onclick = function(event) {
        const addModal = document.getElementById('addModal');
        const editModal = document.getElementById('editModal');
        if (event.target === addModal) {
            closeAddModal();
        }
        if (event.target === editModal) {
            closeEditModal();
        }
    }
    
    // Delete address confirmation using SweetAlert
    document.addEventListener('DOMContentLoaded', function() {
        const deleteButtons = document.querySelectorAll('.delete-address-btn');
        deleteButtons.forEach(function(btn) {
            btn.addEventListener('click', function() {
                const addressId = this.getAttribute('data-id');
                
                swal({
                    title: "Xác nhận xóa",
                    text: "Bạn có chắc muốn xóa địa chỉ này?",
                    icon: "warning",
                    buttons: {
                        cancel: {
                            text: "Hủy",
                            visible: true,
                            className: "swal-button--cancel"
                        },
                        confirm: {
                            text: "Xóa",
                            className: "swal-button--danger"
                        }
                    },
                    dangerMode: true
                }).then((confirmed) => {
                    if (confirmed) {
                        // Create and submit form
                        const form = document.createElement('form');
                        form.method = 'post';
                        form.action = '${pageContext.request.contextPath}/profile/addresses/delete';
                        
                        const input = document.createElement('input');
                        input.type = 'hidden';
                        input.name = 'id';
                        input.value = addressId;
                        
                        form.appendChild(input);
                        document.body.appendChild(form);
                        form.submit();
                    }
                });
            });
        });
    });
    
    // Logout confirmation using SweetAlert
    document.querySelector('.logout-link').addEventListener('click', function(e) {
        e.preventDefault();
        const logoutUrl = this.href;
        
        swal({
            title: "Xác nhận đăng xuất",
            text: "Bạn có chắc muốn đăng xuất?",
            icon: "warning",
            buttons: {
                cancel: {
                    text: "Hủy",
                    visible: true,
                    className: "swal-button--cancel"
                },
                confirm: {
                    text: "Đăng xuất",
                    className: "swal-button--danger"
                }
            },
            dangerMode: true
        }).then((confirmed) => {
            if (confirmed) {
                window.location.href = logoutUrl;
            }
        });
    });
</script>

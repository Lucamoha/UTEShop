<div align="center">

# Website bán sản phẩm công nghệ của Apple (Apple Store)

**Hệ thống thương mại điện tử đa chức năng được phát triển bằng Java**

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![SQL Server](https://img.shields.io/badge/SQL%20Server-2012+-red.svg)](https://www.microsoft.com/sql-server)
[![Tomcat](https://img.shields.io/badge/Tomcat-10+-yellow.svg)](https://tomcat.apache.org/)

</div>

---

## Mục lục

1. [Giới thiệu về đề tài](#1-giới-thiệu-về-đề-tài)
   - [Các chức năng chính tương ứng với người dùng](#các-chức-năng-chính-tương-ứng-với-người-dùng)
     - [Cho Khách hàng](#cho-khách-hàng)
     - [Cho Admin](#cho-admin)
     - [Cho Manager](#cho-manager)
2. [Các công nghệ sử dụng](#2-các-công-nghệ-sử-dụng)
   - [Backend](#backend)
   - [Security & Authentication](#security--authentication)
   - [Frontend](#frontend)
   - [Utilities & Libraries](#utilities--libraries)
   - [Build Tool & Application Server](#build-tool--application-server)
3. [Cấu trúc thư mục](#3-cấu-trúc-thư-mục)
4. [Cách chạy dự án](#4-cách-chạy-dự-án)
   - [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
   - [Các bước cài đặt](#các-bước-cài-đặt)
     - [4.1. Clone dự án](#41-clone-dự-án)
     - [4.2. Cấu hình cơ sở dữ liệu](#42-cấu-hình-cơ-sở-dữ-liệu)
     - [4.3. Deploy lên Application Server](#43-deploy-lên-application-server)
     - [4.4. Truy cập ứng dụng](#44-truy-cập-ứng-dụng)
5. [Video Demo chạy dự án](#5-video-demo-chạy-dự-án)
6. [Đóng góp](#6-đóng-góp)

---

## 1. Giới thiệu về đề tài

**UTEShop** là một hệ thống thương mại điện tử (E-commerce) đa chức năng được phát triển bằng ngôn ngữ lập trình Java. Dự án cung cấp một nền tảng mua sắm trực tuyến hoàn chỉnh với đầy đủ các tính năng từ quản lý sản phẩm, xử lý đơn hàng, thanh toán trực tuyến đến quản trị hệ thống.

### Các chức năng chính tương ứng với người dùng:

#### Cho Khách hàng:

| Chức năng | Mô tả |
|-----------|-------|
| **Xác thực & Bảo mật** | Đăng ký/Đăng nhập (tài khoản thông thường, Google OAuth, Facebook OAuth) |
| **Quên mật khẩu** | Khôi phục tài khoản qua email |
| **Quản lý hồ sơ** | Cập nhật thông tin cá nhân, quản lý địa chỉ giao hàng |
| **Duyệt sản phẩm** | Xem danh sách sản phẩm theo danh mục, tìm kiếm, lọc theo thuộc tính |
| **Chi tiết sản phẩm** | Xem thông tin chi tiết, hình ảnh, các biến thể (variants) |
| **Giỏ hàng** | Thêm/Xóa/Cập nhật sản phẩm trong giỏ hàng |
| **So sánh sản phẩm** | So sánh các sản phẩm cùng danh mục |
| **Đặt hàng** | Tạo đơn hàng với nhiều hình thức thanh toán |
| **Thanh toán trực tuyến** | Tích hợp VNPay và Momo Payment Gateway |
| **Quản lý đơn hàng** | Theo dõi trạng thái, hủy/trả đơn hàng |
| **Đánh giá & Review** | Đánh giá sản phẩm với hình ảnh/video |

#### Cho Admin:

| Chức năng | Mô tả |
|-----------|-------|
| **Dashboard** | Thống kê tổng quan hệ thống |
| **Quản lý danh mục** | CRUD danh mục sản phẩm |
| **Quản lý thuộc tính** | CRUD thuộc tính sản phẩm (attributes) |
| **Quản lý sản phẩm** | CRUD sản phẩm, biến thể, hình ảnh |
| **Quản lý OptionTypes & OptionValues** | Cấu hình các tùy chọn cho biến thể sản phẩm |
| **Quản lý người dùng** | Xem, chỉnh sửa, tìm kiếm người dùng |
| **Quản lý Review** | Duyệt, xóa đánh giá sản phẩm |
| **Quản lý Voucher** | CRUD voucher giảm giá |
| **Quản lý chi nhánh** | CRUD thông tin chi nhánh |

#### Cho Manager:

| Chức năng | Mô tả |
|-----------|-------|
| **Báo cáo doanh thu** | Thống kê doanh thu theo thời gian |
| **Thống kê đơn hàng** | Phân tích trạng thái đơn hàng |
| **Top sản phẩm** | Sản phẩm bán chạy nhất |
| **Quản lý kho** | Import hàng loạt, xuất Excel báo cáo tồn kho |

## 2. Các công nghệ sử dụng

### Backend

| Công nghệ | Mô tả |
|-----------|-------|
| **Java** | Ngôn ngữ lập trình chính |
| **Jakarta Servlet** | Xử lý HTTP requests |
| **Jakarta JSP & JSTL** | View rendering |
| **Hibernate** | ORM Framework |
| **JPA (Jakarta Persistence API)** | Data persistence |
| **Microsoft SQL Server** | Cơ sở dữ liệu quan hệ |
| **MSSQL JDBC Driver** | Database connectivity |

### Security & Authentication

| Công nghệ | Mô tả |
|-----------|-------|
| **JWT (JSON Web Tokens)** | Token-based authentication |
| **BCrypt** | Password hashing |
| **ScribeJava** | OAuth authentication (Google, Facebook) |

### Frontend

| Công nghệ | Mô tả |
|-----------|-------|
| **JSP (JavaServer Pages)** | Server-side rendering |
| **JSTL (JSP Standard Tag Library)** | Template tags |
| **SiteMesh** | Layout & decoration framework |
| **CSS/JavaScript** | Client-side styling và interactivity |

### Utilities & Libraries

| Thư viện | Mục đích sử dụng |
|----------|------------------|
| **Lombok** | Giảm boilerplate code |
| **Apache Commons FileUpload** | File upload handling |
| **Apache Commons IO** | IO utilities |
| **Gson** | JSON serialization/deserialization |
| **Jackson** | JSON processing |
| **Jakarta Mail** | Email sending (forgot password, notifications) |
| **Apache POI** | Excel file processing |
| **OpenPDF** | PDF generation |
| **Hibernate Validator** | Bean validation |

### Build Tool & Application Server

| Công cụ | Mô tả |
|---------|-------|
| **Apache Maven** | Dependency management và build automation |
| **Apache Tomcat 10+** | Jakarta EE compatible application server |

## 3. Cấu trúc thư mục

> **Dự án được xây dựng theo kiến trúc 3 tầng MVC (Model-View-Controller)**

```
UTEShop/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── uteshop/
│       │           ├── api/                    # REST API endpoints
│       │           │   ├── manager/            # Manager APIs (reports, inventory)
│       │           │   └── web/                # Web APIs (place order)
│       │           ├── configs/                # Configuration classes
│       │           ├── controller/             # Servlet Controllers
│       │           │   ├── admin/              # Admin controllers
│       │           │   ├── manager/            # Manager controllers
│       │           │   └── web/                # Web controllers
│       │           ├── dao/                    # Data Access Objects
│       │           ├── dto/                    # Data Transfer Objects
│       │           ├── entity/                 # JPA Entity classes
│       │           │   ├── auth/               # User, UserTokens, Addresses
│       │           │   ├── branch/             # Branch entities
│       │           │   ├── cart/               # Cart, CartItems
│       │           │   ├── catalog/            # Products, Categories, Attributes, Variants
│       │           │   ├── engagement/         # Reviews, Compare
│       │           │   └── order/              # Orders, OrderItems
│       │           ├── enums/                  # Enumeration types
│       │           ├── exception/              # Custom exceptions
│       │           ├── filters/                # Servlet filters (OpenSessionInView, JWTAuthFilter)
│       │           ├── listeners/              # Servlet context listeners
│       │           ├── services/               # Business logic layer
│       │           └── util/                   # Utility classes
│       ├── resources/
│       │   ├── META-INF/
│       │   │   └── persistence.xml             # JPA configuration
│       │   └── config.properties               # Application configuration 
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml                     # Web application descriptor
│           │   └── sitemesh3.xml               # SiteMesh configuration
│           ├── commons/                        # Common JSP fragments
│           ├── templates/                      # SiteMesh layout templates
│           ├── views/                          # JSP view files
│           ├── uploads/                        # Uploaded files (resources images/video)
│           └── index.jsp                       # Landing page
├── target/                                     # Compiled classes (build output)
├── .mvn/                                       # Maven wrapper
├── pom.xml                                     # Maven project configuration
├── mvnw                                        # Maven wrapper script (Unix)
├── mvnw.cmd                                    # Maven wrapper script (Windows)
└── README.md                                   # Project documentation 
```

## 4. Cách chạy dự án

### Yêu cầu hệ thống

| Công cụ | Phiên bản |
|---------|-----------|
| **JDK** | 21 trở lên |
| **Apache Maven** | 3.6+ |
| **Microsoft SQL Server** | 2012+ |
| **Apache Tomcat** | 10+ |
| **IDE** | IntelliJ IDEA / Eclipse / Spring Tool Suite |

### Các bước cài đặt

#### 4.1. Clone dự án

```bash
git clone https://github.com/Lucamoha/UTEShop
cd UTEShop
```

#### 4.2. Cấu hình cơ sở dữ liệu

##### 4.2.1. Tạo database trong SQL Server

```sql
CREATE DATABASE UTEShop;
```

##### 4.2.2. Tạo file cấu hình `config.properties`

Tạo file `src/main/resources/config.properties` với nội dung sau:

```properties
# Database Configuration
db.url=jdbc:sqlserver://localhost:1433;databaseName=UTEShop;encrypt=true;trustServerCertificate=true
db.username=your_username
db.password=your_password

# JWT Secret Key
jwt.secret=your_secret_key_here_at_least_256_bits

# Email Configuration (Dùng để gửi email)
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.username=your_email@gmail.com
mail.password=your_app_password

# OAuth Configuration
# Google OAuth
google.client.id=your_google_client_id
google.client.secret=your_google_client_secret
google.redirect.uri=http://localhost:8080/UTEShop/google-callback

# Facebook OAuth
facebook.client.id=your_facebook_app_id
facebook.client.secret=your_facebook_app_secret
facebook.redirect.uri=http://localhost:8080/UTEShop/facebook-callback

# Payment Gateway Configuration
# VNPay
vnpay.tmnCode=your_vnpay_tmn_code
vnpay.hashSecret=your_vnpay_hash_secret
vnpay.url=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.returnUrl=http://localhost:8080/UTEShop/vnpay-return

# Momo
momo.partnerCode=your_momo_partner_code
momo.accessKey=your_momo_access_key
momo.secretKey=your_momo_secret_key
momo.endpoint=https://test-payment.momo.vn/v2/gateway/api/create
momo.returnUrl=http://localhost:8080/UTEShop/momo-return
momo.notifyUrl=http://localhost:8080/UTEShop/momo-notify

# Application Configuration
app.upload.dir=/path/to/upload/directory
```

#### 4.3. Deploy lên Application Server

##### Sử dụng IDE (IntelliJ IDEA)

**Bước 1:** Mở dự án trong IntelliJ IDEA

**Bước 2:** Cấu hình Tomcat server
- Vào `Run` → `Edit Configurations` → `Add New Configuration` → `Tomcat Server` → `Local`
- Chọn Tomcat installation directory
- Trong tab `Deployment`, add artifact: `UTEShop:war exploded`
- Application context: `/UTEShop`

**Bước 3:** Click `Run` để khởi chạy

#### 4.4. Truy cập ứng dụng

Mở trình duyệt và truy cập các URL sau:

| Trang | URL |
|-------|-----|
| **Trang chủ** | http://localhost:8080/UTEShop/ |
| **Admin Panel** | http://localhost:8080/UTEShop/admin/dashboard |
| **Manager Panel** | http://localhost:8080/UTEShop/manager/reports |

---

## 5. Video Demo chạy dự án

```
Link video: 
```

**Nội dung video demo bao gồm:**


---

## 6. Đóng góp

Dự án được phát triển bởi nhóm sinh viên Đại học Sư phạm Kỹ thuật TP.HCM (UTE):

| Thành viên | GitHub |
|------------|--------|
| **Trần Triều Dương** | [@Lucamoha](https://github.com/Lucamoha) |
| **Võ Lê Khánh Duy** | [@DuyVo-2005](https://github.com/DuyVo-2005) |
| **Văn Phú Hiền** | [@VanPhuHien](https://github.com/VanPhuHien) |
| **Nguyễn Văn Kế** | [@nvk3005](https://github.com/nvk3005) |

---

<div align="center">

**UTEShop** - Website bán sản phẩm công nghệ của Apple

Đồ án Lập trình Web | Năm học 2024-2025

</div>


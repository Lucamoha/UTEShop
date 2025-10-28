<div align="center">

# Website bán sản phẩm công nghệ của Apple (Apple Store)

**Hệ thống thương mại điện tử đa chức năng được phát triển bằng Java**

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)
[![SQL Server](https://img.shields.io/badge/SQL%20Server-2019+-red.svg)](https://www.microsoft.com/sql-server)
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

| Chức năng                 | Mô tả                                                                    |
| ------------------------- | ------------------------------------------------------------------------ |
| **Xác thực & Bảo mật**    | Đăng ký/Đăng nhập (tài khoản thông thường, Google OAuth, Facebook OAuth) |
| **Quên mật khẩu**         | Khôi phục tài khoản qua email                                            |
| **Quản lý hồ sơ**         | Cập nhật thông tin cá nhân, quản lý địa chỉ giao hàng                    |
| **Duyệt sản phẩm**        | Xem danh sách sản phẩm theo danh mục, tìm kiếm, lọc theo thuộc tính      |
| **Chi tiết sản phẩm**     | Xem thông tin chi tiết, hình ảnh, các biến thể (variants)                |
| **Giỏ hàng**              | Thêm/Xóa/Cập nhật sản phẩm trong giỏ hàng                                |
| **So sánh sản phẩm**      | So sánh các sản phẩm cùng danh mục                                       |
| **Đặt hàng**              | Tạo đơn hàng với nhiều hình thức thanh toán                              |
| **Thanh toán trực tuyến** | Tích hợp VNPay và Momo Payment Gateway                                   |
| **Quản lý đơn hàng**      | Theo dõi trạng thái, hủy/trả đơn hàng                                    |
| **Đánh giá & Review**     | Đánh giá sản phẩm với hình ảnh/video                                     |

#### Cho Admin:

| Chức năng                              | Mô tả                                       |
| -------------------------------------- | ------------------------------------------- |
| **Dashboard**                          | Thống kê tổng quan hệ thống                 |
| **Quản lý danh mục**                   | CRUD danh mục sản phẩm                      |
| **Quản lý thuộc tính**                 | CRUD thuộc tính sản phẩm (attributes)       |
| **Quản lý sản phẩm**                   | CRUD sản phẩm, biến thể, hình ảnh           |
| **Quản lý OptionTypes & OptionValues** | Cấu hình các tùy chọn cho biến thể sản phẩm |
| **Quản lý người dùng**                 | Xem, chỉnh sửa, tìm kiếm người dùng         |
| **Quản lý Review**                     | Duyệt, xóa đánh giá sản phẩm                |
| **Quản lý Voucher**                    | CRUD voucher giảm giá                       |
| **Quản lý chi nhánh**                  | CRUD thông tin chi nhánh                    |

#### Cho Manager:

| Chức năng             | Mô tả                                        |
| --------------------- | -------------------------------------------- |
| **Báo cáo doanh thu** | Thống kê doanh thu theo thời gian            |
| **Thống kê đơn hàng** | Phân tích trạng thái đơn hàng                |
| **Top sản phẩm**      | Sản phẩm bán chạy nhất                       |
| **Quản lý kho**       | Import hàng loạt, xuất Excel báo cáo tồn kho |

## 2. Các công nghệ sử dụng

### Backend

| Công nghệ                         | Mô tả                    |
| --------------------------------- | ------------------------ |
| **Java**                          | Ngôn ngữ lập trình chính |
| **Jakarta Servlet**               | Xử lý HTTP requests      |
| **Jakarta JSP & JSTL**            | View rendering           |
| **Hibernate**                     | ORM Framework            |
| **JPA (Jakarta Persistence API)** | Data persistence         |
| **Microsoft SQL Server**          | Cơ sở dữ liệu quan hệ    |
| **MSSQL JDBC Driver**             | Database connectivity    |

### Security & Authentication

| Công nghệ                 | Mô tả                                   |
| ------------------------- | --------------------------------------- |
| **JWT (JSON Web Tokens)** | Token-based authentication              |
| **BCrypt**                | Password hashing                        |
| **ScribeJava**            | OAuth authentication (Google, Facebook) |

### Frontend

| Công nghệ                           | Mô tả                                |
| ----------------------------------- | ------------------------------------ |
| **JSP (JavaServer Pages)**          | Server-side rendering                |
| **JSTL (JSP Standard Tag Library)** | Template tags                        |
| **SiteMesh**                        | Layout & decoration framework        |
| **CSS/JavaScript**                  | Client-side styling và interactivity |

### Utilities & Libraries

| Thư viện                      | Mục đích sử dụng                               |
| ----------------------------- | ---------------------------------------------- |
| **Lombok**                    | Giảm boilerplate code                          |
| **Apache Commons FileUpload** | File upload handling                           |
| **Apache Commons IO**         | IO utilities                                   |
| **Gson**                      | JSON serialization/deserialization             |
| **Jackson**                   | JSON processing                                |
| **Jakarta Mail**              | Email sending (forgot password, notifications) |
| **Apache POI**                | Excel file processing                          |
| **OpenPDF**                   | PDF generation                                 |
| **Hibernate Validator**       | Bean validation                                |

### Build Tool & Application Server

| Công cụ               | Mô tả                                     |
| --------------------- | ----------------------------------------- |
| **Apache Maven**      | Dependency management và build automation |
| **Apache Tomcat 10+** | Jakarta EE compatible application server  |

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

Trước khi cài đặt, cần chuẩn bị các công cụ sau:

| Thành phần                  | Phiên bản khuyến nghị | Ghi chú                                  |
| --------------------------- | --------------------- | ---------------------------------------- |
| **JDK**                     | 17+                   | Thiết lập biến môi trường                |
| **Apache Tomcat**           | 10.1.x                | Servlet API 5.0 (phù hợp với jakarta.\*) |
| **SQL Server**              | 2019 hoặc mới hơn     | Dùng để lưu trữ dữ liệu ứng dụng         |
| **Maven**                   | 3.9+                  | Quản lý dependencies                     |
| **IntelliJ IDEA / Eclipse** | Mới nhất              | IDE để chạy và debug (tùy chọn)          |

### Các bước cài đặt

#### 4.1. Clone dự án

```bash
git clone https://github.com/Lucamoha/UTEShop
cd UTEShop
```

#### 4.2. Cấu hình cơ sở dữ liệu

##### 4.2.1. Tạo database trong SQL Server

Trong thư mục `src/main/resources/database`, thực thi script `UTEShop.sql`

##### 4.2.2. Thiết lập file cấu hình `config.properties`

Trong thư mục `src/main/resources/`, tạo file mới tên là `config.properties` (copy từ file mẫu `config.properties.example`) và cập nhật nội dung phù hợp với môi trường:

```properties
# ========================
# Database Config
# ========================
db.url=jdbc:sqlserver://{your_servername_here}:1433;databaseName=UTEShop;encrypt=true;trustServerCertificate=true
db.username=your_username_here
db.password=your_password_here

# ========================
# JWT Config
# ========================
jwt.signerKey=your_secret_here
jwt.expiration=3600000

# ========================
# Momo config
# ========================
momo.partnerCode=MOMO
momo.accessKey=F8BBA842ECF85
momo.secretKey=K951B6PE1waDMi640xX08PD3vg6EkVlz
momo.endpointCreate=https://test-payment.momo.vn/v2/gateway/api/create
momo.endpointQuery=https://test-payment.momo.vn/v2/gateway/api/query
momo.endpointRefund=https://test-payment.momo.vn/v2/gateway/api/refund
momo.redirectUrl={your_domain}/payment/momo/return
momo.ipnUrl={your_domain}/payment/momo/ipn

# ========================
# Vnpay config
# ========================
vnpay.vnp_Version=2.1.0
vnpay.vnp_Command=pay
vnpay.vnp_TmnCode=your_vnp_TmnCode_here
vnpay.vnp_HashSecret=your_vnp_HashSecret_here
vnpay.vnp_CurrCode=VND
vnpay.vnp_OrderType=billpayment
vnpay.vnp_Url=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.vnp_Query=https://sandbox.vnpayment.vn/merchant_webapi/api/transaction
vnpay.vnp_ReturnUrl={your_domain}/payment/vnpay/return
vnpay.vnp_IpAddr=127.0.0.1

# ========================
# GOOGLE OAUTH
# ========================
google.clientId=your_google_clientId_here
google.clientSecret=your_google_clientSecret_here
google.redirectUri={your_domain}/login/oauth/google/callback
google.scope=openid email profile

# ========================
# FACEBOOK AUTH
# ========================
facebook.clientId=your_facebook_clientId_here
facebook.clientSecret=your_facebook_clientSecret_here
facebook.redirectUri={your_domain}/login/oauth/facebook/callback
facebook.scope=email,public_profile

# ========================
# Email/SMTP Config
# ========================
smtp.host=smtp.gmail.com
smtp.port=587
smtp.from.email=your-email@gmail.com
smtp.from.password=your-app-password
smtp.from.name=UTEShop

# ========================
# Upload Directory
# ========================
upload.dir={Your Upload Directory}
```

**Lưu ý quan trọng:**

- **Không public file `config.properties`** lên Git/GitHub
- **Momo Config:** Các giá trị `momo.partnerCode`, `momo.accessKey`, `momo.secretKey` là các giá trị test được Momo cung cấp cho môi trường sandbox. Tham khảo thêm tại: [Momo Developer](https://developers.momo.vn/v3/vi/docs/payment/guides/home)
- **VnPay Config:** Tham khảo doc cho môi trường test tại: [VnPay APIs](https://sandbox.vnpayment.vn/apis/)
  - Trang Merchant Admin để quản lý giao dịch: [VnPay Merchant](https://sandbox.vnpayment.vn/merchantv2/)
  - Set IpnUrl tại: [VnPay Login](https://sandbox.vnpayment.vn/vnpaygw-sit-testing/user/login)
- **Google OAuth:** Tham khảo tại: [Google Cloud Console](https://console.cloud.google.com/)
- **Facebook OAuth:** Tham khảo tại: [Facebook Developers](https://developers.facebook.com/)

#### 4.3. Deploy lên Application Server

<details>
<summary><strong>Cách 1: Sử dụng IDE (IntelliJ IDEA)</strong></summary>

**Bước 1:** Mở dự án trong IntelliJ IDEA

**Bước 2:** Cấu hình Tomcat server

- Vào `Run` → `Edit Configurations` → `Add New Configuration` → `Tomcat Server` → `Local`
- Chọn Tomcat installation directory
- Trong tab `Deployment`, add artifact: `UTEShop:war exploded`
- Application context: `/UTEShop`

**Bước 3:** Click `Run` để khởi chạy

</details>

<details>
<summary><strong>Cách 2: Dùng Maven CLI</strong></summary>

**Bước 1:** Build project bằng Maven

```bash
mvn clean package
```

**Bước 2:** Deploy file `.war`

Sau khi build thành công, file `UTEShop-1.0-SNAPSHOT.war` sẽ được tạo trong thư mục `target/`

- Copy file `.war` vào thư mục `webapps` của Tomcat
- Khởi động Tomcat server

```bash
# Windows
cd %CATALINA_HOME%\bin
startup.bat

# Linux/macOS
cd $CATALINA_HOME/bin
./startup.sh
```

Tomcat sẽ tự động deploy ứng dụng khi khởi động.

</details>

#### 4.4. Truy cập ứng dụng

Mở trình duyệt và truy cập các URL sau:

| Trang             | URL                                           |
| ----------------- | --------------------------------------------- |
| **Trang chủ**     | http://localhost:8080/UTEShop/                |
| **Admin Panel**   | http://localhost:8080/UTEShop/admin/dashboard |
| **Manager Panel** | http://localhost:8080/UTEShop/manager/dashboard |

---

## 5. Video Demo chạy dự án

```
Link video:
```

**Nội dung video demo bao gồm:**

---

## 6. Đóng góp

Dự án được phát triển bởi nhóm sinh viên Đại học Sư phạm Kỹ thuật TP.HCM (UTE):

| Thành viên           | GitHub                                       |
| -------------------- | -------------------------------------------- |
| **Trần Triều Dương** | [@Lucamoha](https://github.com/Lucamoha)     |
| **Võ Lê Khánh Duy**  | [@DuyVo-2005](https://github.com/DuyVo-2005) |
| **Văn Phú Hiền**     | [@VanPhuHien](https://github.com/VanPhuHien) |
| **Nguyễn Văn Kế**    | [@nvk3005](https://github.com/nvk3005)       |

---

<div align="center">

**UTEShop** - Website bán sản phẩm công nghệ của Apple

Đồ án Lập trình WEB

</div>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>So sánh - UTEShop</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
            background: #ffffff;
            color: #1d1d1f;
            line-height: 1.5;
            -webkit-font-smoothing: antialiased;
        }

        .comparison-header {
            background: #fbfbfd;
            border-bottom: 1px solid #d2d2d7;
            padding: 20px 0;
            position: sticky;
            top: 0;
            z-index: 100;
        }

        .header-content {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .header-title {
            font-size: 28px;
            font-weight: 600;
            color: #1d1d1f;
            letter-spacing: -0.5px;
        }

        .header-back {
            color: #0071e3;
            text-decoration: none;
            font-size: 17px;
            transition: opacity 0.2s;
        }

        .header-back:hover {
            opacity: 0.7;
        }

        .comparison-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 60px 20px;
        }

        .category-section {
            text-align: center;
            margin-bottom: 50px;
        }

        .category-label {
            font-size: 14px;
            color: #86868b;
            text-transform: uppercase;
            letter-spacing: 0.8px;
            font-weight: 500;
        }

        .comparison-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 30px;
            margin-bottom: 60px;
        }

        .product-card {
            background: #fbfbfd;
            border-radius: 18px;
            overflow: hidden;
            transition: box-shadow 0.3s ease;
        }

        .product-card:hover {
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        }

        .product-image-section {
            background: #ffffff;
            padding: 40px 20px;
            text-align: center;
            min-height: 320px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .product-image {
            max-width: 260px;
            max-height: 260px;
            object-fit: contain;
        }

        .product-info {
            padding: 30px;
            text-align: center;
            border-bottom: 1px solid #d2d2d7;
        }

        .product-name {
            font-size: 24px;
            font-weight: 600;
            color: #1d1d1f;
            margin-bottom: 8px;
            letter-spacing: -0.3px;
        }

        .product-name a {
            color: #1d1d1f;
            text-decoration: none;
            transition: color 0.2s;
        }

        .product-name a:hover {
            color: #0071e3;
        }

        .product-price {
            font-size: 17px;
            color: #1d1d1f;
            font-weight: 400;
        }

        .specs-section {
            padding: 30px;
        }

        .spec-row {
            display: flex;
            padding: 16px 0;
            border-bottom: 1px solid #d2d2d7;
        }

        .spec-row:last-child {
            border-bottom: none;
        }

        .spec-label {
            flex: 1;
            font-size: 14px;
            color: #86868b;
            font-weight: 400;
        }

        .spec-value {
            flex: 1;
            font-size: 14px;
            color: #1d1d1f;
            font-weight: 500;
            text-align: right;
        }

        .actions {
            text-align: center;
            margin-top: 40px;
        }

        .btn {
            display: inline-block;
            padding: 12px 24px;
            border-radius: 980px;
            text-decoration: none;
            font-size: 17px;
            font-weight: 400;
            transition: all 0.2s;
            margin: 0 8px;
        }

        .btn-primary {
            background: #0071e3;
            color: #ffffff;
        }

        .btn-primary:hover {
            background: #0077ed;
        }

        .btn-secondary {
            background: transparent;
            color: #0071e3;
            border: 1px solid #0071e3;
        }

        .btn-secondary:hover {
            background: #0071e3;
            color: #ffffff;
        }

        .error-container {
            text-align: center;
            padding: 100px 20px;
        }

        .error-message {
            font-size: 21px;
            color: #1d1d1f;
            margin-bottom: 20px;
        }

        .error-description {
            font-size: 17px;
            color: #86868b;
            margin-bottom: 30px;
        }

        @media (max-width: 768px) {
            .comparison-grid {
                grid-template-columns: 1fr;
                gap: 20px;
            }

            .header-title {
                font-size: 24px;
            }

            .product-name {
                font-size: 21px;
            }

            .product-image-section {
                min-height: 280px;
                padding: 30px 20px;
            }

            .product-image {
                max-width: 220px;
                max-height: 220px;
            }

            .specs-section {
                padding: 20px;
            }

            .btn {
                display: block;
                margin: 8px 0;
            }
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .comparison-grid {
            animation: fadeIn 0.5s ease;
        }
    </style>
</head>
<body>
    <div class="comparison-header">
        <div class="header-content">
             <a href="javascript:history.back()" class="header-back">
                ← Quay lại
            </a>

            <h1 class="header-title">So sánh</h1>
            <div style="width: 100px;"></div>
        </div>
    </div>

    <c:if test="${not empty error}">
        <div class="error-container">
            <div class="error-message">Không thể so sánh</div>
            <div class="error-description">${error}</div>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                Quay lại trang chủ
            </a>
        </div>
    </c:if>

    <c:if test="${not empty comparison}">
        <div class="comparison-container">
            <div class="category-section">
                <div class="category-label">${comparison.categoryName}</div>
            </div>

            <div class="comparison-grid">
                <!-- Product 1 -->
                <div class="product-card">
                    <div class="product-image-section">
                        <c:choose>
                            <c:when test="${not empty comparison.product1Image}">
                                <img src="${pageContext.request.contextPath}/image?fname=${comparison.product1Image}" 
                                     alt="${comparison.product1Name}" 
                                     class="product-image">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/templates/images/no-image.png" 
                                     alt="No image" 
                                     class="product-image">
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="product-info">
                        <div class="product-name">
                            <a href="${pageContext.request.contextPath}/product/${comparison.product1Slug}">
                                ${comparison.product1Name}
                            </a>
                        </div>
                        <div class="product-price">
                            <fmt:formatNumber value="${comparison.product1Price}" pattern="#,###" /> VND
                        </div>
                    </div>

                    <div class="specs-section">
                        <c:forEach items="${comparison.comparableAttributes}" var="attr">
                            <div class="spec-row">
                                <div class="spec-label">
                                    ${attr.attributeName}
                                </div>
                                <div class="spec-value">
                                    <c:choose>
                                        <c:when test="${empty attr.product1Value or attr.product1Value == null}">
                                            N/A
                                        </c:when>
                                        <c:when test="${attr.dataType == 1}">
                                            ${attr.product1Value}
                                        </c:when>
                                        <c:when test="${attr.dataType == 2}">
                                            <c:choose>
                                                <c:when test="${not empty attr.unit and attr.product1Value != 'N/A'}">
                                                    <fmt:formatNumber value="${attr.product1Value}" type="number" maxFractionDigits="2" />
                                                    (${attr.unit})
                                                </c:when>
                                                <c:otherwise>
                                                    ${attr.product1Value}
                                                    <c:if test="${not empty attr.unit and attr.product1Value == 'N/A'}">
                                                        (${attr.unit})
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:when test="${attr.dataType == 3}">
                                            <c:choose>
                                                <c:when test="${attr.product1Value == '1'}">
                                                    Có
                                                </c:when>
                                                <c:otherwise>
                                                    Không
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            ${attr.product1Value}
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- Product 2 -->
                <div class="product-card">
                    <div class="product-image-section">
                        <c:choose>
                            <c:when test="${not empty comparison.product2Image}">
                                <img src="${pageContext.request.contextPath}/image?fname=${comparison.product2Image}" 
                                     alt="${comparison.product2Name}" 
                                     class="product-image">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/templates/images/no-image.png" 
                                     alt="No image" 
                                     class="product-image">
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="product-info">
                        <div class="product-name">
                            <a href="${pageContext.request.contextPath}/product/${comparison.product2Slug}">
                                ${comparison.product2Name}
                            </a>
                        </div>
                        <div class="product-price">
                            <fmt:formatNumber value="${comparison.product2Price}" pattern="#,###" /> VND
                        </div>
                    </div>

                    <div class="specs-section">
                        <c:forEach items="${comparison.comparableAttributes}" var="attr">
                            <div class="spec-row">
                                <div class="spec-label">
                                    ${attr.attributeName}
                                </div>
                                <div class="spec-value">
                                    <c:choose>
                                        <c:when test="${empty attr.product2Value or attr.product2Value == null}">
                                            N/A
                                        </c:when>
                                        <c:when test="${attr.dataType == 1}">
                                            ${attr.product2Value}
                                        </c:when>
                                        <c:when test="${attr.dataType == 2}">
                                            <c:choose>
                                                <c:when test="${not empty attr.unit and attr.product2Value != 'N/A'}">
                                                    <fmt:formatNumber value="${attr.product2Value}" type="number" maxFractionDigits="2" />
                                                    (${attr.unit})
                                                </c:when>
                                                <c:otherwise>
                                                    ${attr.product2Value}
                                                    <c:if test="${not empty attr.unit and attr.product2Value == 'N/A'}">
                                                        (${attr.unit})
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:when test="${attr.dataType == 3}">
                                            <c:choose>
                                                <c:when test="${attr.product2Value == '1'}">
                                                    Có
                                                </c:when>
                                                <c:otherwise>
                                                    Không
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            ${attr.product2Value}
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="actions">
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                    Trang chủ
                </a>
                <a href="javascript:history.back()" class="btn btn-secondary">
                    Quay lại
                </a>
            </div>
        </div>
    </c:if>
</body>
</html>

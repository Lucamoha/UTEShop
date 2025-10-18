package com.uteshop.enums;

public final class OrderEnums {
    private OrderEnums() {}

    public static final class OrderStatus {
        public static final int NEW = 0, CONFIRMED = 1, SHIPPING = 2, DELIVERED = 3, CANCELED = 4, RETURNED = 5;

        public static String label(int m) {
            return switch (m) {
                case NEW -> "Mới tạo";
                case CONFIRMED -> "Đã xác nhận";
                case SHIPPING -> "Đang vận chuyển";
                case DELIVERED -> "Đã nhận";
                case CANCELED -> "Đã hủy";
                case RETURNED -> "Trả hàng";
                default -> "UNKNOWN";
            };
        }
    }

    public static final class PaymentStatus {
        public static final int UNPAID = 0, PAID = 1, REFUNDED = 2;

        public static String label(int m) {
            return switch (m) {
                case UNPAID -> "Chưa thanh toán";
                case PAID -> "Đã thanh toán";
                case REFUNDED -> "Hoàn tiền";
                default -> "UNKNOWN";
            };
        }
    }
}

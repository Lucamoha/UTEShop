package com.uteshop.enums;

public final class PaymentEnums {
    private PaymentEnums() {}

    public static final class Status {
        public static final int PENDING = 0, SUCCESS = 1, FAILED = 2, REFUNDED = 3;

        public static String label(int m) {
            return switch (m) {
                case PENDING -> "Đang chờ";
                case SUCCESS -> "Thành công";
                case FAILED -> "Thất bại";
                case REFUNDED -> "Hoàn tiền";
                default -> "UNKNOWN";
            };
        }
    }

    public static final class Method {
        public static final int COD = 0, VNPAY = 1, MOMO = 2;

        public static String label(int m) {
            return switch (m) {
                case COD -> "COD";
                case VNPAY -> "VNPAY";
                case MOMO -> "MOMO";
                default -> "UNKNOWN";
            };
        }
    }
}

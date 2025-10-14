package com.uteshop.enums;

public final class OrderEnums {
    private OrderEnums() {}

    public static final class OrderStatus {
        public static final int NEW = 0, CONFIRMED = 1, SHIPPING = 2, DELIVERED = 3, CANCELED = 4, RETURNED = 5;
    }

    public static final class PaymentStatus {
        public static final int UNPAID = 0, PAID = 1, REFUNDED = 2;
    }
}

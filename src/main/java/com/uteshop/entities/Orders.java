package com.uteshop.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    Users user;

    int OrderStatus;

    int PaymentStatus;

    BigDecimal Subtotal;

    BigDecimal DiscountAmount;

    BigDecimal ShippingFee;

    BigDecimal TotalAmount;

    String VoucherCode;

    String ReceiverName;

    String Phone;

    String AddressLine;

    String Ward;

    String District;

    String City;

    String Note;

    LocalDateTime CreatedAt;

    LocalDateTime UpdatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<OrderItems> items = new ArrayList<>();
}


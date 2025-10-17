package com.uteshop.entity.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payments implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    // Mỗi payment thuộc 1 order
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OrderId", nullable = false, unique = true)
    Orders order;

    /**
     * 0 = COD
     * 1 = VNPAY
     * 2 = MoMo
     */
    @Column(nullable = false)
    int Method;

    /**
     * 0 = pending
     * 1 = success
     * 2 = failed
     * 3 = refunded
     */
    @Column(nullable = false)
    int Status;

    // Mã giao dịch của cổng thanh toán
    @Column(length = 120)
    String TxnId;

    @Column(length = 120)
    String refundTxnId;

    // Số tiền đã thanh toán / hoàn tiền
    @Column(precision = 12, scale = 2)
    BigDecimal PaidAmount;

    LocalDateTime PaidAt;

    // Dữ liệu trả về từ provider (có thể JSON)
    @Column(columnDefinition = "NVARCHAR(MAX)")
    String ProviderRaw;

    @Column(nullable = false)
    LocalDateTime CreatedAt;

    @PrePersist
    void onCreate() {
        CreatedAt = LocalDateTime.now();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

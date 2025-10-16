package com.uteshop.entity.cart;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Vouchers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vouchers implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @Column(nullable = false, unique = true, length = 40)
    String Code;

    @Column(columnDefinition = "NVARCHAR(255)")
    String DescText;

    @Column(nullable = false)
    Integer Type; // 1=Percent, 2=Amount

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal Value;

    @Builder.Default
    @Column(nullable = false)
    Integer TotalUsed = 0;

    @Builder.Default
    @Column(nullable = false)
    Integer MaxUses = 1000;

    @Column(nullable = false)
    LocalDateTime StartsAt;

    @Column(nullable = false)
    LocalDateTime EndsAt;

    @Builder.Default
    @Column(nullable = false)
    Boolean IsActive = true;

    /**
     * Kiểm tra voucher có hợp lệ không
     */
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return IsActive
                && now.isAfter(StartsAt)
                && now.isBefore(EndsAt)
                && TotalUsed < MaxUses;
    }

    /**
     * Tính discount amount dựa trên type và subtotal
     * @param subtotal Tổng tiền hàng
     * @return Số tiền được giảm
     */
    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        if (!isValid()) {
            return BigDecimal.ZERO;
        }

        switch (Type) {
            case 1: // Percent
                BigDecimal discount = subtotal.multiply(Value).divide(BigDecimal.valueOf(100));
                return discount;

            case 2: // Amount (fixed discount)
                return Value.min(subtotal); // Không vượt quá subtotal

            default:
                return BigDecimal.ZERO;
        }
    }

    /**
     * Get voucher type name
     */
    public String getTypeName() {
        switch (Type) {
            case 1:
                return "Giảm " + Value.intValue() + "%";
            case 2:
                return "Giảm " + Value.longValue() + "đ";
            default:
                return "Unknown";
        }
    }
}

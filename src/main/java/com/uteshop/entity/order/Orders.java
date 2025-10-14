package com.uteshop.entity.order;

import com.uteshop.entity.auth.Users;
import com.uteshop.entity.branch.Branches;
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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    Users user;

    @Column(nullable = false)
    int OrderStatus = 0;

    @Column(nullable = false)
    int PaymentStatus = 0;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal Subtotal;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal DiscountAmount = BigDecimal.valueOf(0);

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal ShippingFee = BigDecimal.valueOf(0);

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal TotalAmount;

    String VoucherCode;

    @Column(nullable = false, columnDefinition = "NVARCHAR(120)")
    String ReceiverName;

    @Column(nullable = false, length = 20)
    String Phone;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    String AddressLine;

    @Column(columnDefinition = "NVARCHAR(100)")
    String Ward;

    @Column(columnDefinition = "NVARCHAR(100)")
    String District;

    @Column(columnDefinition = "NVARCHAR(100)")
    String City;

    @Column(columnDefinition = "NVARCHAR(400)")
    String Note;

    @Column(nullable = false)
    LocalDateTime CreatedAt;

    LocalDateTime UpdatedAt;
    
    @PrePersist
	void onCreate() {
		//Tự động gán giá trị khi insert mới
		CreatedAt = LocalDateTime.now();
		UpdatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	void onUpdate() {
		// Tự động cập nhật UpdatedAt khi update
		UpdatedAt = LocalDateTime.now();
	}

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<OrderItems> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BranchId")
    Branches branch;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, optional = true)
    Payments payment;
}


package com.uteshop.entity.auth;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Addresses {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)//khi nào dùng đến cột thì nạp, không nạp hết cột từ đâu
    @JoinColumn(name = "UserId", nullable = false)
    Users user;

    String Label;

    @Column(nullable = false, columnDefinition = "NVARCHAR(120)")
    String FullName;

    @Column(nullable = false, length = 20)
    String Phone;

    @Column(nullable = false)
    String AddressLine;

    String Ward;

    String District;

    String City;

    @Column(nullable = false)
    Boolean IsDefault = false;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

package com.uteshop.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
//@Table(name = "CompareLists", schema = "dbo") // nếu bảng tên CompareLists
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompareLists implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)//fetch = FetchType.LAZY -> Dữ liệu quan hệ chỉ được tải khi bạn thực sự gọi đến
    //vd: hi bạn gọi order.getCustomer(), lúc đó mới query Customer từ DB
    //khác với FetchType.EAGER Dữ liệu quan hệ sẽ được load cùng lúc khi bạn query entity chính.
    @JoinColumn(name = "UserId", nullable = false)
    Users user;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<CompareItems> items = new ArrayList<>();
}

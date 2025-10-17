package com.uteshop.entity.catalog;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionTypes implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @Column(nullable = false, unique = true, length = 30)
    String Code;
    
    @OneToMany(mappedBy = "optionType", fetch = FetchType.EAGER)
    private List<OptionValues> values;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

package com.uteshop.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionValues implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    int Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OptionTypeId", nullable = false)
    OptionTypes optionType;

    String Value;
}

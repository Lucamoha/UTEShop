package com.uteshop.entity.catalog;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NamedQuery(name = "Attributes.findAll", query = "select a from Attributes a")
public class Attributes implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(120)")
    String Name;

    @Column(nullable = false)
    int DataType; // 1=text,2=number,3=boolean

    String Unit;
}

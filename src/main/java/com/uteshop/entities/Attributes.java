package com.uteshop.entities;

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
    int Id;

    String Name;

    int DataType; // 1=text,2=number,3=boolean

    String Unit;
}

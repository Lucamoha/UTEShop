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
@NamedQuery(name = "SystemSettings.findAll", query = "select s from SystemSettings s")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id 
    String Key;

    String Value;
}

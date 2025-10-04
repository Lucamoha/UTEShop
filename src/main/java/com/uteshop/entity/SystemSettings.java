package com.uteshop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "SystemSettings.findAll", query = "select s from SystemSettings s")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "[Key]")
    String Key;

    @Column(name = "[Value]")
    String Value;
}

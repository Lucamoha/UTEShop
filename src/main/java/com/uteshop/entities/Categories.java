package com.uteshop.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NamedQueries({
    @NamedQuery(name = "Categories.findAll", 
                query = "SELECT c FROM Categories c"),
                
    @NamedQuery(name = "Categories.findParents", 
                query = "SELECT c FROM Categories c WHERE c.parent IS NULL"),
                
    @NamedQuery(name = "Categories.findChildren", 
                query = "SELECT c FROM Categories c WHERE c.parent.id = :parentId"),
                
    @NamedQuery(name = "Categories.findBySlug", 
                query = "SELECT c FROM Categories c WHERE c.Slug = :slug")
})
public class Categories implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    int Id;

    String Name;

    String Slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentId")
    Categories parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Categories> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Products> products = new ArrayList<>();
}

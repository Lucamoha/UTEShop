package com.uteshop.entity.catalog;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"parent", "children", "categoryAttributes"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

	@Column(nullable = false, columnDefinition = "NVARCHAR(120)")
    String Name;

	@Column(nullable = false, unique = true)
    String Slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentId")
    Categories parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Categories> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Products> products = new ArrayList<>();
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<CategoryAttributes> categoryAttributes = new ArrayList<>();

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

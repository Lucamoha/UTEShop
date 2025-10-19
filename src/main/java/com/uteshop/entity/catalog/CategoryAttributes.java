package com.uteshop.entity.catalog;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@ToString(exclude = {"category", "attribute"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryAttributes implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    Id id;

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId", nullable = false)
    Categories category;

    @MapsId("attributeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AttributeId", nullable = false)
    Attributes attribute;

    @Column(nullable = false)
    Boolean IsFilterable = true;

    @Column(nullable = false)
    Boolean IsComparable = true;

    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "CategoryId")
        private Integer categoryId;

        @Column(name = "AttributeId")
        private Integer attributeId;
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return Objects.equals(categoryId, id.categoryId) &&
                   Objects.equals(attributeId, id.attributeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(categoryId, attributeId);
        }

        @Override
        public String toString() {
            return "Id{" +
                    "categoryId=" + categoryId +
                    ", attributeId=" + attributeId +
                    '}';
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

package com.uteshop.entities;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompareItems implements Serializable {
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    CompareItemId id;

    @MapsId("listId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ListId", nullable = false)
    CompareLists list;

}

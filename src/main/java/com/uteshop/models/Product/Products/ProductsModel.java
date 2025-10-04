package com.uteshop.models.Product.Products;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductsModel implements Serializable {

	private static final long serialVersionUID = 1L;

    int Id;

    String Name;

    String Slug;

    String Description;

    BigDecimal BasePrice;

    boolean Status;
    
    int CategoryId;
    
    boolean IsEdit;
}

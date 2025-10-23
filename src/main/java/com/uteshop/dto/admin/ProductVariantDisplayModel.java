package com.uteshop.dto.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

//Tạo class view để hiển thị cho JSP
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantDisplayModel {
	Integer id;
	String sku;
	BigDecimal price;
	Boolean status;
	List<String> options = new ArrayList<>();
	List<Integer> optionTypeIds = new ArrayList<>();
	List<Integer> optionValueIds = new ArrayList<>();
}

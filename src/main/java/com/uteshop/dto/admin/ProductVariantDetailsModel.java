package com.uteshop.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantDetailsModel {
	Integer Id;
	Integer ProductId;
	String SKU;
	BigDecimal Price;
	Boolean Status;
	LocalDateTime CreatedAt;
	LocalDateTime UpdatedAt;
	Integer OptionTypeId;
	String Code;
	Integer OptionValueId;
	String Value;
}

package com.uteshop.dto.admin;

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
public class ProductAttributeDisplayModel {
	Integer productId;
	Integer attributeId;
	String name;
	Integer dataType;
	String unit;
	String valueText;
	BigDecimal valueNumber;

	public String getDisplayValue() {
		if (valueText != null && !valueText.isBlank()) {
			return valueText + (unit != null ? " " + unit : "");
		}
		if (valueNumber != null) {
			return valueNumber.stripTrailingZeros().toPlainString() + (unit != null ? " " + unit : "");
		}
		return "";
	}
}

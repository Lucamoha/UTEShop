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
	Integer ProductId;
	Integer AttributeId;
	String Name;
	Integer DataType;
	String Unit;
	String ValueText;
	BigDecimal ValueNumber;

	public String getDisplayValue() {
		if (ValueText != null && !ValueText.isBlank()) {
			return ValueText + (Unit != null ? " " + Unit : "");
		}
		if (ValueNumber != null) {
			return ValueNumber.stripTrailingZeros().toPlainString() + (Unit != null ? " " + Unit : "");
		}
		return "";
	}
}

package com.uteshop.dto.web;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDto {
    private int optionTypeId;
    private String optionTypeCode;
    private int optionValueId;
    private String optionValue;
}

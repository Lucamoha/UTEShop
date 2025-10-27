package com.uteshop.services.web.catalog;

import com.uteshop.dto.web.OptionDto;

import java.util.List;

public interface IOptionsService {
    List<OptionDto> getOptionsByProduct(int productId);
}

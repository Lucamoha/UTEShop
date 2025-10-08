package com.uteshop.services.Option;

import com.uteshop.dto.web.OptionDto;

import java.util.List;

public interface IOptionsService {
    List<OptionDto> getOptionsByProduct(int productId);
}

package com.uteshop.dao;

import com.uteshop.dto.web.OptionDto;

import java.util.List;

public interface IOptionsDao {
    List<OptionDto> getOptionsByProduct(int productId);
}

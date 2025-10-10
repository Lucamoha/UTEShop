package com.uteshop.dao.web;

import com.uteshop.dto.web.OptionDto;

import java.util.List;

public interface IOptionsDao {
    List<OptionDto> getOptionsByProduct(int productId);
}

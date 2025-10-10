package com.uteshop.services.impl.web;

import com.uteshop.dao.impl.web.OptionsDaoImpl;
import com.uteshop.dao.web.IOptionsDao;
import com.uteshop.dto.web.OptionDto;
import com.uteshop.services.web.IOptionsService;

import java.util.List;

public class OptionsServiceImpl implements IOptionsService {
    IOptionsDao optionsDao = new OptionsDaoImpl();
    @Override
    public List<OptionDto> getOptionsByProduct(int productId) {
        return optionsDao.getOptionsByProduct(productId);
    }
}

package com.uteshop.services.impl.web.catalog;

import com.uteshop.dao.impl.web.catalog.OptionsDaoImpl;
import com.uteshop.dao.web.catalog.IOptionsDao;
import com.uteshop.dto.web.OptionDto;
import com.uteshop.services.web.catalog.IOptionsService;

import java.util.List;

public class OptionsServiceImpl implements IOptionsService {
    IOptionsDao optionsDao = new OptionsDaoImpl();
    @Override
    public List<OptionDto> getOptionsByProduct(int productId) {
        return optionsDao.getOptionsByProduct(productId);
    }
}

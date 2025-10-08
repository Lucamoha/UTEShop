package com.uteshop.services.impl.Option;

import com.uteshop.dao.IOptionsDao;
import com.uteshop.dao.impl.Option.OptionsDaoImpl;
import com.uteshop.dto.web.OptionDto;
import com.uteshop.services.Option.IOptionsService;

import java.util.List;

public class OptionsServiceImpl implements IOptionsService {
    IOptionsDao optionsDao = new OptionsDaoImpl();
    @Override
    public List<OptionDto> getOptionsByProduct(int productId) {
        return optionsDao.getOptionsByProduct(productId);
    }
}

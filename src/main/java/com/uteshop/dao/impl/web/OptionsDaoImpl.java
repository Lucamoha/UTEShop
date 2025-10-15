package com.uteshop.dao.impl.web;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.web.IOptionsDao;
import com.uteshop.dto.web.OptionDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

public class OptionsDaoImpl implements IOptionsDao {

    @Override
    public List<OptionDto> getOptionsByProduct(int productId) {
        EntityManager enma = JPAConfigs.getEntityManager();
        try {
            Query query = enma.createNativeQuery("EXEC sp_GetOptionsByProduct :productId");
            query.setParameter("productId", productId);

            List<Object[]> results = query.getResultList();

            return results.stream()
                    .map(row -> new OptionDto(
                            ((Number) row[0]).intValue(), // OptionTypeId
                            (String) row[1], // OptionTypeCode
                            ((Number) row[2]).intValue(), // OptionValueId
                            (String) row[3] // OptionValue
                    ))
                    .toList();
        } finally {
            enma.close();
        }
    }

    public static void main(String[] args) {
        IOptionsDao dao = new OptionsDaoImpl();
        List<OptionDto> options = dao.getOptionsByProduct(1);
        for (OptionDto option : options) {
            System.out.println(option.getOptionTypeCode());
        }
    }
}

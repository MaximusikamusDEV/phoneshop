package com.es.core.model.phone.ResultSetExtractors;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Mappers.ColorMapper;
import com.es.core.model.phone.Mappers.PhoneMapper;
import com.es.core.model.phone.Phone;
import jakarta.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

@Component
public class PhoneSetExtractor implements ResultSetExtractor<List<Phone>> {
    @Resource
    private PhoneMapper phoneMapper;
    @Resource
    private ColorMapper colorMapper;

    @Override
    public List<Phone> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Phone> phoneMap = new HashMap<>();

        while (rs.next()) {
            Long phoneId = rs.getLong("id");
            Phone phone = phoneMap.get(phoneId);

            if (phone == null) {
                phone = phoneMapper.mapRow(rs);
                phone.setColors(new HashSet<>());
                phoneMap.put(phoneId, phone);
            }

            Long colorId = (Long) rs.getObject("color_id");

            if (colorId != null) {
                Color color = colorMapper.mapRow(rs);
                phone.getColors().add(color);
            }
        }
        return new ArrayList<>(phoneMap.values());
    }
}

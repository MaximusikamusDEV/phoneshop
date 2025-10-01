package com.es.core.model.phone.ResultSetExtractors;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
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
public class ManyPhonesWithColors extends PhoneExtractor implements ResultSetExtractor<List<Phone>> {
    @Override
    public List<Phone> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Phone> phoneMap = new HashMap<>();

        while (rs.next()) {
            Long phoneId = rs.getLong("id");
            Phone phone = phoneMap.get(phoneId);

            if (phone == null) {
                phone = mapResultSetToPhone(rs);
                phone.setColors(new HashSet<>());
                phoneMap.put(phoneId, phone);
            }

            rs.getLong("color_id");

            if (!rs.wasNull()) {
                Color color = mapResultSetToColor(rs);
                phone.getColors().add(color);
            }
        }
        return new ArrayList<>(phoneMap.values());
    }
}

package com.es.core.model.phone.ResultSetExtractors;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@Component
public class SinglePhoneWithColors extends PhoneExtractor implements ResultSetExtractor<Phone> {
    @Override
    public Phone extractData(ResultSet rs) throws SQLException, DataAccessException {
        Phone currPhone = null;

        while (rs.next()) {
            if (currPhone == null) {
                currPhone = mapResultSetToPhone(rs);
                currPhone.setColors(new HashSet<>());
            }

            rs.getLong("color_id");

            if (!rs.wasNull()) {
                Color color = mapResultSetToColor(rs);
                currPhone.getColors().add(color);
            }
        }
        return currPhone;
    }
}

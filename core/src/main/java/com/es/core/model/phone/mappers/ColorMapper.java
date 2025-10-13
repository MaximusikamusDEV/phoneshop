package com.es.core.model.phone.mappers;

import com.es.core.model.phone.Color;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ColorMapper {
    public Color mapRow(final ResultSet rs) throws SQLException {
        Color color = new Color();
        color.setId(rs.getLong("color_id"));
        color.setCode(rs.getString("color_code"));
        return color;
    }
}

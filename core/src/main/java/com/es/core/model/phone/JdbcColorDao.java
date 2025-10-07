package com.es.core.model.phone;

import com.es.core.model.Constants.DBConstants;
import com.es.core.model.Constants.ExceptionConstants;
import com.es.core.model.Exceptions.DatabaseUpdateException;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class JdbcColorDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void savePhoneColors(Phone phone) {
        if (phone.getColors() != null) {
            phone.getColors().forEach(color -> {
                if (color.getId() == null) {
                    getOrCreateColorId(color);
                }

                jdbcTemplate.update(DBConstants.QUERY_INSERT_PHONE_COLOR, phone.getId(), color.getId());
            });
        }
    }

    private void getOrCreateColorId(Color color) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(color);
        namedParameterJdbcTemplate.update(DBConstants.QUERY_SAVE_COLOR, parameters, keyHolder, new String[]{"id"});

        Optional<Number> id = Optional.ofNullable(keyHolder.getKey());

        id.ifPresentOrElse(number -> color.setId(number.longValue()),
                () -> {
                    throw new DatabaseUpdateException(ExceptionConstants.DATABASE_SAVE_PROBLEM);
                });
    }
}

package com.es.core.model.phone;

import com.es.core.model.constants.DBConstants;
import com.es.core.model.constants.ExceptionConstants;
import com.es.core.model.exceptions.DatabaseException;
import com.es.core.model.exceptions.DatabaseUpdateException;
import com.es.core.model.phone.resultSetExtractors.PhoneSetExtractor;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao {
    @Resource
    private PhoneSetExtractor phoneSetExtractor;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private JdbcColorDao jdbcColorDao;

    @Override
    public Optional<Phone> get(final Long key) {
        List<Phone> phones = jdbcTemplate.query(DBConstants.QUERY_GET_PHONE_WITH_COLORS, phoneSetExtractor, key);

        return CollectionUtils.emptyIfNull(phones).stream().findFirst();
    }

    @Override
    @Transactional
    public void save(final Phone phone) {
        if (phone.getId() == null) {
            newPhoneIdFromDb(phone);
        }
        else {
            if (isExistingPhone(phone)) {
                jdbcTemplate.update(DBConstants.QUERY_DELETE_PHONE_COLORS, phone.getId());
                jdbcColorDao.savePhoneColors(phone);
            } else {
                newPhoneIdFromDb(phone);
            }
        }
    }

    @Override
    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(DBConstants.QUERY_FIND_ALL, phoneSetExtractor, offset, limit);
    }

    @SuppressWarnings("SqlSourceToSinkFlow")
    @Override
    public List<Phone> findAllInStockSorted(String query, int offset, int limit, String sortField, String sortOrder) {
        return Optional.ofNullable(query)
                .map(q ->
                        jdbcTemplate.query(
                                String.format(
                                        DBConstants.QUERY_FIND_ALL_BY_QUERY_IN_STOCK_SORTED,
                                        sortField,
                                        sortOrder,
                                        sortField,
                                        sortOrder),
                                phoneSetExtractor, q, q, offset, limit)
                ).orElseGet(() ->
                        jdbcTemplate.query(String.format(
                                        DBConstants.QUERY_FIND_ALL_IN_STOCK_SORTED,
                                        sortField,
                                        sortOrder,
                                        sortField,
                                        sortOrder),
                                phoneSetExtractor, offset, limit)
                );
    }

    @Override
    public int getCountPhoneInStock(String query) {
        Integer countResult = Optional.ofNullable(query)
                .map(q -> jdbcTemplate.queryForObject(
                        DBConstants.QUERY_COUNT_PHONES_BY_QUERY_IN_STOCK,
                        Integer.class,
                        q,
                        q))
                .orElseGet(() ->
                        jdbcTemplate.queryForObject(
                                DBConstants.QUERY_COUNT_PHONES_IN_STOCK,
                                Integer.class));

        return Optional.ofNullable(countResult).orElseThrow(() -> new DatabaseException(ExceptionConstants.DATABASE_PROBLEM));
    }

    private void newPhoneIdFromDb(Phone phone) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(phone);
        namedParameterJdbcTemplate.update(DBConstants.QUERY_SAVE_PHONE, parameters, keyHolder, new String[]{"id"});

        Optional<Number> id = Optional.ofNullable(keyHolder.getKey());

        id.ifPresentOrElse(number -> {
                    phone.setId(number.longValue());
                    jdbcColorDao.savePhoneColors(phone);
                },
                () -> {
                    throw new DatabaseUpdateException(ExceptionConstants.DATABASE_SAVE_PROBLEM);
                });
    }

    private boolean isExistingPhone(final Phone phone) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(phone);
        int rowsUpdated = namedParameterJdbcTemplate.update(DBConstants.QUERY_UPDATE_PHONE, parameters);

        return rowsUpdated != 0;
    }
}

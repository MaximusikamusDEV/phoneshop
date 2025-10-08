package com.es.core.model.phone;

import com.es.core.model.Constants.DBConstants;
import com.es.core.model.Constants.ExceptionConstants;
import com.es.core.model.Exceptions.DatabaseUpdateException;
import com.es.core.model.phone.ResultSetExtractors.PhoneSetExtractor;
import jakarta.annotation.Resource;
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
    JdbcColorDao jdbcColorDao;

    @Override
    public Optional<Phone> get(final Long key) {
        List<Phone> phones = jdbcTemplate.query(DBConstants.QUERY_GET_PHONE_WITH_COLORS, phoneSetExtractor, key);

        return phones.stream().findFirst();
    }

    @Override
    @Transactional
    public void save(final Phone phone) {
        if (phone.getId() == null) {
            newPhoneIdFromDb(phone);
        } else {
            if (isExistingPhone(phone)) {
                newPhoneIdFromDb(phone);
            } else {
                jdbcTemplate.update(DBConstants.QUERY_DELETE_PHONE_COLORS, phone.getId());
                jdbcColorDao.savePhoneColors(phone);
            }
        }
    }

    @Override
    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(DBConstants.QUERY_FIND_ALL_WITH_COLORS_OFFSET_LIMIT, phoneSetExtractor, offset, limit);
    }

    @Override
    public List<Phone> findAllInStockSorted(int offset, int limit, String sortField, String sortOrder) {
        String query = String.format(
                DBConstants.QUERY_FIND_ALL_WITH_COLORS_OFFSET_LIMIT_IN_STOCK_SORTED,
                sortField,
                sortOrder,
                sortField,
                sortOrder);

        return jdbcTemplate.query(query, phoneSetExtractor, offset, limit);
    }

    @Override
    public int getPhoneInStockCount() {
        Optional<Integer> countResult = Optional.ofNullable(jdbcTemplate.queryForObject(DBConstants.QUERY_COUNT_PHONES_IN_STOCK, Integer.class));
        return countResult.orElse(0);
    }

    @Override
    public List<Phone> findAllByQueryInStock(String query, int offset, int limit, String sortField, String sortOrder) {
        String dbQuery = String.format(DBConstants.QUERY_BY_QUERY_IN_STOCK, sortField, sortOrder, sortField, sortOrder);
        return jdbcTemplate.query(dbQuery, phoneSetExtractor, query, query, offset, limit);
    }

    @Override
    public int getCountPhoneByQueryInStock(String query) {
        Optional<Integer> countResult
                = Optional.ofNullable(
                        jdbcTemplate.queryForObject(
                                DBConstants.QUERY_COUNT_PHONES_IN_STOCK_WITH_QUERY,
                                Integer.class,
                                query,
                                query));

        return countResult.orElse(0);
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

        return rowsUpdated == 0;
    }
}

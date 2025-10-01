package com.es.core.model.phone;

import com.es.core.model.phone.ResultSetExtractors.ManyPhonesWithColors;
import com.es.core.model.phone.ResultSetExtractors.SinglePhoneWithColors;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private static final String QUERY_GET_PHONE_WITH_COLORS = "select " +
            "p.id, p.brand, p.model, p.price, p.displaySizeInches, p.weightGr, " +
            "p.lengthMm, p.widthMm, p.heightMm, p.announced, p.deviceType, p.os, " +
            "p.displayResolution, p.pixelDensity, p.displayTechnology, " +
            "p.backCameraMegapixels, p.frontCameraMegapixels, p.ramGb, " +
            "p.internalStorageGb, p.batteryCapacityMah, p.talkTimeHours, " +
            "p.standByTimeHours, p.bluetooth, p.positioning, p.imageUrl, p.description, " +
            "c.id as color_id, c.code as color_code " +
            "from phones p " +
            "left join phone2color on p.id = phone2color.phoneId " +
            "left join colors c on phone2color.colorId = c.id " +
            "where p.id = ? " +
            "order by p.id";
    private static final String QUERY_FIND_ALL_WITH_COLORS_OFFSET_LIMIT = "select " +
            "p.id, p.brand, p.model, p.price, p.displaySizeInches, p.weightGr, " +
            "p.lengthMm, p.widthMm, p.heightMm, p.announced, p.deviceType, p.os, " +
            "p.displayResolution, p.pixelDensity, p.displayTechnology, " +
            "p.backCameraMegapixels, p.frontCameraMegapixels, p.ramGb, " +
            "p.internalStorageGb, p.batteryCapacityMah, p.talkTimeHours, " +
            "p.standByTimeHours, p.bluetooth, p.positioning, p.imageUrl, p.description, " +
            "c.id as color_id, c.code as color_code " +
            "from phones p " +
            "left join phone2color on p.id = phone2color.phoneId " +
            "left join colors c on phone2color.colorId = c.id " +
            "order by p.id OFFSET ? LIMIT ?";
    private static final String QUERY_SAVE_PHONE = "insert into phones (brand, model, price, displaySizeInches, weightGr," +
            "lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity," +
            "displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb," +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description)" +
            "values (:brand, :model, :price, :displaySizeInches, :weightGr, " +
            ":lengthMm, :widthMm, :heightMm, :announced, :deviceType, :os, :displayResolution, :pixelDensity, " +
            ":displayTechnology, :backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, " +
            ":batteryCapacityMah, :talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";
    private static final String QUERY_UPDATE_PHONE =
            "update phones set brand = :brand, model = :model, price = :price, " +
                    "displaySizeInches = :displaySizeInches, weightGr = :weightGr, " +
                    "lengthMm = :lengthMm, widthMm = :widthMm, heightMm = :heightMm, " +
                    "announced = :announced, deviceType = :deviceType, os = :os, " +
                    "displayResolution = :displayResolution, pixelDensity = :pixelDensity, " +
                    "displayTechnology = :displayTechnology, backCameraMegapixels = :backCameraMegapixels, " +
                    "frontCameraMegapixels = :frontCameraMegapixels, ramGb = :ramGb, " +
                    "internalStorageGb = :internalStorageGb, batteryCapacityMah = :batteryCapacityMah, " +
                    "talkTimeHours = :talkTimeHours, standByTimeHours = :standByTimeHours, " +
                    "bluetooth = :bluetooth, positioning = :positioning, imageUrl = :imageUrl, " +
                    "description = :description where id = :id";
    private static final String QUERY_DELETE_PHONE_COLORS = "delete from phone2color where phoneId = ?";
    private static final String QUERY_INSERT_PHONE_COLOR = "insert into phone2color (phoneId, colorId) values (?, ?)";
    private static final String QUERY_SAVE_COLOR = "insert into colors (code) values (:code)";
    private static final String DATABASE_SAVE_PROBLEM = "Problem saving data into database";
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(JdbcPhoneDao.class);

    @Override
    public Optional<Phone> get(final Long key) {
        Phone finalPhone = jdbcTemplate.query(QUERY_GET_PHONE_WITH_COLORS, new SinglePhoneWithColors(), key);

        return Optional.ofNullable(finalPhone);
    }

    @Override
    @Transactional
    public void save(final Phone phone) {
        if (phone.getId() == null) {
            newPhoneIdFromDb(phone);
        } else {
            SqlParameterSource parameters = new BeanPropertySqlParameterSource(phone);
            int rowsUpdated = namedParameterJdbcTemplate.update(QUERY_UPDATE_PHONE, parameters);

            if (rowsUpdated == 0) {
                newPhoneIdFromDb(phone);
            } else {
                jdbcTemplate.update(QUERY_DELETE_PHONE_COLORS, phone.getId());
                savePhoneColors(phone);
            }
        }
    }

    @Override
    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(QUERY_FIND_ALL_WITH_COLORS_OFFSET_LIMIT, new ManyPhonesWithColors(), offset, limit);
    }

    private void savePhoneColors(Phone phone) {
        if (phone.getColors() != null) {
            phone.getColors().forEach(color -> {
                if (color.getId() == null) {
                    getOrCreateColorId(color);
                }

                jdbcTemplate.update(QUERY_INSERT_PHONE_COLOR, phone.getId(), color.getId());
            });
        }
    }

    private void getOrCreateColorId(Color color) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(color);
        namedParameterJdbcTemplate.update(QUERY_SAVE_COLOR, parameters, keyHolder, new String[]{"id"});

        Optional<Number> id = Optional.ofNullable(keyHolder.getKey());

        id.ifPresentOrElse(number -> {
                    color.setId(number.longValue());
                },
                () ->
                {
                    logger.error(DATABASE_SAVE_PROBLEM);
                    throw new DatabaseUpdateException(DATABASE_SAVE_PROBLEM);
                });
    }

    private void newPhoneIdFromDb(Phone phone) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(phone);
        namedParameterJdbcTemplate.update(QUERY_SAVE_PHONE, parameters, keyHolder, new String[]{"id"});

        Optional<Number> id = Optional.ofNullable(keyHolder.getKey());

        id.ifPresentOrElse(number -> {
                    phone.setId(number.longValue());
                    savePhoneColors(phone);
                },
                () ->
                {
                    logger.error(DATABASE_SAVE_PROBLEM);
                    throw new DatabaseUpdateException(DATABASE_SAVE_PROBLEM);
                });
    }
}

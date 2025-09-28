package com.es.core.model.phone;

import jakarta.annotation.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private static final String QUERY_GET_PHONE = "select * from phones where id = ?";
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
    private static final String QUERY_GET_COLORS = "select colors.id, colors.code from colors " +
            "join phone2color on colors.id = phone2color.colorId " +
            "where phone2color.phoneId = ?";
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
    private static final String PHONE_ID_EMPTY = "Phone id is empty";
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Phone> get(final Long key) {
        try {
            Phone phone = jdbcTemplate.queryForObject(QUERY_GET_PHONE, new BeanPropertyRowMapper<>(Phone.class), key);

            if (phone != null) {
                List<Color> colors = jdbcTemplate.query(QUERY_GET_COLORS, new BeanPropertyRowMapper<>(Color.class), phone.getId());
                phone.setColors(new HashSet<>(colors));
            }

            return Optional.ofNullable(phone);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public void save(final Phone phone) {
        if (phone.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource parameters = new BeanPropertySqlParameterSource(phone);
            namedParameterJdbcTemplate.update(QUERY_SAVE_PHONE, parameters, keyHolder, new String[]{"id"});

            Number id = keyHolder.getKey();

            if (id != null) {
                phone.setId(id.longValue());
            }
        } else {
            SqlParameterSource parameters = new BeanPropertySqlParameterSource(phone);
            namedParameterJdbcTemplate.update(QUERY_UPDATE_PHONE, parameters);
        }

        savePhoneColors(phone);
    }

    public List<Phone> findAll(int offset, int limit) {
        Map<Long, Phone> phoneMap = new HashMap<>();

        jdbcTemplate.query(QUERY_FIND_ALL_WITH_COLORS_OFFSET_LIMIT, rs -> {
            Long phoneId = rs.getLong("id");
            Phone phone = phoneMap.get(phoneId);

            if (phone == null) {
                phone = mapResultSetToPhone(rs);
                phone.setColors(new HashSet<>());
                phoneMap.put(phoneId, phone);
            }

            long colorId = rs.getLong("color_id");

            if (colorId != 0) {
                Color color = mapResultSetToColor(rs);
                phone.getColors().add(color);
            }
        }, offset, limit);

        return new ArrayList<>(phoneMap.values());
    }

    private Color mapResultSetToColor(ResultSet rs) throws SQLException {
        Color color = new Color();
        color.setId(rs.getLong("color_id"));
        color.setCode(rs.getString("color_code"));
        return color;
    }

    private Phone mapResultSetToPhone(final ResultSet rs) throws SQLException {
        Phone phone = new Phone();
        phone.setId(rs.getLong("id"));
        phone.setBrand(rs.getString("brand"));
        phone.setModel(rs.getString("model"));
        phone.setPrice(rs.getBigDecimal("price"));
        phone.setDisplaySizeInches(rs.getBigDecimal("displaySizeInches"));
        phone.setWeightGr(rs.getInt("weightGr"));
        phone.setWidthMm(rs.getBigDecimal("widthMm"));
        phone.setLengthMm(rs.getBigDecimal("lengthMm"));
        phone.setHeightMm(rs.getBigDecimal("heightMm"));
        phone.setAnnounced(rs.getDate("announced"));
        phone.setDeviceType(rs.getString("deviceType"));
        phone.setOs(rs.getString("os"));
        phone.setDisplayResolution(rs.getString("displayResolution"));
        phone.setPixelDensity(rs.getInt("pixelDensity"));
        phone.setDisplayTechnology(rs.getString("displayTechnology"));
        phone.setBackCameraMegapixels(rs.getBigDecimal("backCameraMegapixels"));
        phone.setFrontCameraMegapixels(rs.getBigDecimal("frontCameraMegapixels"));
        phone.setRamGb(rs.getBigDecimal("ramGb"));
        phone.setInternalStorageGb(rs.getBigDecimal("internalStorageGb"));
        phone.setBatteryCapacityMah(rs.getInt("batteryCapacityMah"));
        phone.setTalkTimeHours(rs.getBigDecimal("talkTimeHours"));
        phone.setStandByTimeHours(rs.getBigDecimal("standByTimeHours"));
        phone.setBluetooth(rs.getString("bluetooth"));
        phone.setPositioning(rs.getString("positioning"));
        phone.setImageUrl(rs.getString("imageUrl"));
        phone.setDescription(rs.getString("description"));
        return phone;
    }

    private void savePhoneColors(Phone phone) {
        jdbcTemplate.update(QUERY_DELETE_PHONE_COLORS, phone.getId());

        if (phone.getColors() != null && !phone.getColors().isEmpty()) {
            for (Color color : phone.getColors()) {
                jdbcTemplate.update(QUERY_INSERT_PHONE_COLOR, phone.getId(), color.getId());
            }
        }
    }
}

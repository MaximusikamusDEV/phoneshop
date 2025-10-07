package com.es.core.model.Constants;

public class DBConstants {
    public static final String QUERY_GET_PHONE_WITH_COLORS = "select " +
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
    public static final String QUERY_FIND_ALL_WITH_COLORS_OFFSET_LIMIT = "select " +
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
    public static final String QUERY_SAVE_PHONE = "insert into phones (brand, model, price, displaySizeInches, weightGr," +
            "lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity," +
            "displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb," +
            "batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description)" +
            "values (:brand, :model, :price, :displaySizeInches, :weightGr, " +
            ":lengthMm, :widthMm, :heightMm, :announced, :deviceType, :os, :displayResolution, :pixelDensity, " +
            ":displayTechnology, :backCameraMegapixels, :frontCameraMegapixels, :ramGb, :internalStorageGb, " +
            ":batteryCapacityMah, :talkTimeHours, :standByTimeHours, :bluetooth, :positioning, :imageUrl, :description)";
    public static final String QUERY_UPDATE_PHONE =
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
    public static final String QUERY_DELETE_PHONE_COLORS = "delete from phone2color where phoneId = ?";
    public static final String QUERY_INSERT_PHONE_COLOR = "insert into phone2color (phoneId, colorId) values (?, ?)";
    public static final String QUERY_SAVE_COLOR = "insert into colors (code) values (:code)";
}

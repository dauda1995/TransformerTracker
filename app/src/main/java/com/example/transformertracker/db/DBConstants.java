package com.example.transformertracker.db;

import android.provider.BaseColumns;

public final class DBConstants {
     private DBConstants(){}

            static final String TX_TABLE = "transformers";
         static final String TX_ID = "tx_id";
         static final String TX_NAME = "name";
         static final public String TX_LOCATION = "location";
         static final public String TX_VOLTAGERATIO = "voltage";
         static final public String TX_FEEDER = "feeder";
         static final public String TX_ZONE = "zone";
         static final public String TX_RATING = "rating";
         static final public String TX_BRAND = "brand";
         static final public String TX_LATITUDE = "latitude";
         static final public String TX_LONGITUDE = "longitude";
         static final public String TX_STATUS = "status";
         static final public String TX_SERIAL_NO = "serialNo";
         static final public String TX_METER_NO = "meterNo";
         static final public String TX_YEAR = "year";
         static final public String TX_TIME = "timeStamp";
         static final public String TX_CAT = "category";
         static final public String TX_IMAGE_ID = "imageID";

         static final String CREATE_TX_TABLE = "CREATE TABLE IF NOT EXISTS " + TX_TABLE + " ("
                 + TX_ID + " INTEGER PRIMARY KEY,"
                 + TX_NAME + " TEXT,"
                 + TX_LOCATION + " TEXT,"
                 + TX_VOLTAGERATIO + " TEXT,"
                 + TX_FEEDER + " TEXT,"
                 + TX_ZONE + " TEXT,"
                 + TX_RATING + " TEXT,"
                 + TX_BRAND + " TEXT,"
                 + TX_LATITUDE + " TEXT,"
                 + TX_LONGITUDE + " TEXT,"
                 + TX_STATUS + " TEXT,"
                 + TX_SERIAL_NO + " TEXT,"
                 + TX_METER_NO + " TEXT,"
                 + TX_YEAR + " TEXT,"
                 + TX_TIME + " TEXT,"
                 + TX_CAT + " TEXT,"
                 + TX_IMAGE_ID + " BLOB DEFAULT NULL)";

         static final String SELECT_QUERY = "SELECT * FROM " + TX_TABLE;


 }

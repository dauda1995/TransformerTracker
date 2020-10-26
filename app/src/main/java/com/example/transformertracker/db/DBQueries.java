package com.example.transformertracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.transformertracker.model.Transformer;

import java.util.ArrayList;

public class DBQueries {

    private Context context;
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DBQueries(Context context) {
        this.context = context;
    }

    public DBQueries open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // Users
    public boolean insertUser(Transformer tx) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.TX_NAME, tx.name);
        values.put(DBConstants.TX_ZONE, tx.zone);
        values.put(DBConstants.TX_FEEDER, tx.feeder);
        values.put(DBConstants.TX_LOCATION, tx.location);
        values.put(DBConstants.TX_RATING, tx.rating);
        values.put(DBConstants.TX_VOLTAGERATIO, tx.voltageRatio);
        values.put(DBConstants.TX_LATITUDE, tx.latitude);
        values.put(DBConstants.TX_LONGITUDE, tx.longitude);
        values.put(DBConstants.TX_BRAND, tx.brand);
        values.put(DBConstants.TX_SERIAL_NO, tx.serialNo);
        values.put(DBConstants.TX_METER_NO, tx.meterNo);
        values.put(DBConstants.TX_STATUS, tx.status);
        values.put(DBConstants.TX_CAT, tx.category);
        values.put(DBConstants.TX_YEAR, tx.year);
        values.put(DBConstants.TX_TIME, tx.time);
        values.put(DBConstants.TX_IMAGE_ID, tx.imageUri);
        return database.insert(DBConstants.TX_TABLE, null, values) > -1;
    }

    public ArrayList<Transformer> readUsers() {
        ArrayList<Transformer> list = new ArrayList<>();
        try {
            Cursor cursor;
            database = dbHelper.getReadableDatabase();
            cursor = database.rawQuery(DBConstants.SELECT_QUERY, null);
            list.clear();
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String txId = cursor.getString(cursor.getColumnIndex(DBConstants.TX_ID));
                        String name = cursor.getString(cursor.getColumnIndex(DBConstants.TX_NAME));
                        String location = cursor.getString(cursor.getColumnIndex(DBConstants.TX_LOCATION));
                        String feeder = cursor.getString(cursor.getColumnIndex(DBConstants.TX_FEEDER));
                        String zone = cursor.getString(cursor.getColumnIndex(DBConstants.TX_ZONE));
                        String rating = cursor.getString(cursor.getColumnIndex(DBConstants.TX_RATING));
                        String voltageRat = cursor.getString(cursor.getColumnIndex(DBConstants.TX_VOLTAGERATIO));
                        String brand = cursor.getString(cursor.getColumnIndex(DBConstants.TX_BRAND));
                        String serialNo = cursor.getString(cursor.getColumnIndex(DBConstants.TX_SERIAL_NO));
                        String meterNo = cursor.getString(cursor.getColumnIndex(DBConstants.TX_METER_NO));
                        Double latitude = cursor.getDouble(cursor.getColumnIndex(DBConstants.TX_LATITUDE));
                        Double longitude = cursor.getDouble(cursor.getColumnIndex(DBConstants.TX_LONGITUDE));
                        String status = cursor.getString(cursor.getColumnIndex(DBConstants.TX_STATUS));
                        String year = cursor.getString(cursor.getColumnIndex(DBConstants.TX_YEAR));
                        String timeStamp = cursor.getString(cursor.getColumnIndex(DBConstants.TX_TIME));
                        String category = cursor.getString(cursor.getColumnIndex(DBConstants.TX_CAT));
                        String image = cursor.getString(cursor.getColumnIndex(DBConstants.TX_IMAGE_ID));
//                        byte[] blobb = cursor.getBlob(cursor.getColumnIndex(DBConstants.CONTACT_NO));
                        Transformer transformers = new Transformer(txId, name,  location, voltageRat,
                                feeder, zone, rating, brand, latitude, longitude, status
                        , serialNo, meterNo, year, timeStamp, category, image);
                        list.add(transformers);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.v("Exception", e.getMessage());
        }
        return list;
    }

}

package com.example.transformertracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.transformertracker.db.DBConstants.*;

import com.example.transformertracker.model.Transformer;

import java.util.ArrayList;

import static com.example.transformertracker.db.DBConstants.TX_BRAND;
import static com.example.transformertracker.db.DBConstants.TX_CAT;
import static com.example.transformertracker.db.DBConstants.TX_FEEDER;
import static com.example.transformertracker.db.DBConstants.TX_ID;
import static com.example.transformertracker.db.DBConstants.TX_IMAGE_ID;
import static com.example.transformertracker.db.DBConstants.TX_LATITUDE;
import static com.example.transformertracker.db.DBConstants.TX_LOCATION;
import static com.example.transformertracker.db.DBConstants.TX_LONGITUDE;
import static com.example.transformertracker.db.DBConstants.TX_METER_NO;
import static com.example.transformertracker.db.DBConstants.TX_NAME;
import static com.example.transformertracker.db.DBConstants.TX_RATING;
import static com.example.transformertracker.db.DBConstants.TX_SERIAL_NO;
import static com.example.transformertracker.db.DBConstants.TX_STATUS;
import static com.example.transformertracker.db.DBConstants.TX_TIME;
import static com.example.transformertracker.db.DBConstants.TX_VOLTAGERATIO;
import static com.example.transformertracker.db.DBConstants.TX_YEAR;
import static com.example.transformertracker.db.DBConstants.TX_ZONE;

public class DBQueries  implements LoaderManager.LoaderCallbacks<Void> {

    private Context context;
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public static final int READ_DATA = 0;
    public static final int WRITE_DATA = 1;

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
        values.put(TX_NAME, tx.name);
        values.put(TX_ZONE, tx.zone);
        values.put(TX_FEEDER, tx.feeder);
        values.put(TX_LOCATION, tx.location);
        values.put(TX_RATING, tx.rating);
        values.put(TX_VOLTAGERATIO, tx.voltageRatio);
        values.put(TX_LATITUDE, tx.latitude);
        values.put(TX_LONGITUDE, tx.longitude);
        values.put(TX_BRAND, tx.brand);
        values.put(TX_SERIAL_NO, tx.serialNo);
        values.put(TX_METER_NO, tx.meterNo);
        values.put(TX_STATUS, tx.status);
        values.put(TX_CAT, tx.category);
        values.put(TX_YEAR, tx.year);
        values.put(TX_TIME, tx.time);
        values.put(TX_IMAGE_ID, tx.imageUri);
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
                        String txId = cursor.getString(cursor.getColumnIndex(TX_ID));
                        String name = cursor.getString(cursor.getColumnIndex(TX_NAME));
                        String location = cursor.getString(cursor.getColumnIndex(TX_LOCATION));
                        String feeder = cursor.getString(cursor.getColumnIndex(TX_FEEDER));
                        String zone = cursor.getString(cursor.getColumnIndex(TX_ZONE));
                        String rating = cursor.getString(cursor.getColumnIndex(TX_RATING));
                        String voltageRat = cursor.getString(cursor.getColumnIndex(TX_VOLTAGERATIO));
                        String brand = cursor.getString(cursor.getColumnIndex(TX_BRAND));
                        String serialNo = cursor.getString(cursor.getColumnIndex(TX_SERIAL_NO));
                        String meterNo = cursor.getString(cursor.getColumnIndex(TX_METER_NO));
                        Double latitude = cursor.getDouble(cursor.getColumnIndex(TX_LATITUDE));
                        Double longitude = cursor.getDouble(cursor.getColumnIndex(TX_LONGITUDE));
                        String status = cursor.getString(cursor.getColumnIndex(TX_STATUS));
                        String year = cursor.getString(cursor.getColumnIndex(TX_YEAR));
                        String timeStamp = cursor.getString(cursor.getColumnIndex(TX_TIME));
                        String category = cursor.getString(cursor.getColumnIndex(TX_CAT));
                        String image = cursor.getString(cursor.getColumnIndex(TX_IMAGE_ID));
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


    @NonNull
    @Override
    public Loader<Void> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Void> loader, Void data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Void> loader) {

    }
}

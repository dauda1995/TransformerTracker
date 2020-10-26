package com.example.transformertracker.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Transformer implements Parcelable {
    public String uid;
    public String name;
    public String location;
    public String voltageRatio;
    public String feeder;
    public String zone;
    public String rating;
    public String brand;
    public Double latitude;
    public Double longitude;
    public String status;
    public String serialNo;
    public String meterNo;
    public String year;
    public String time;
    public String category;
    public String imageUri;

    public Transformer() {
    }

    public Transformer(String uid, String name, String location, String voltageRatio, String feeder, String zone, String rating, String brand, Double latitude, Double longitude, String status, String serialNo, String meterNo, String year, String time, String category, String imageUri) {
        this.uid = uid;
        this.name = name;
        this.location = location;
        this.voltageRatio = voltageRatio;
        this.feeder = feeder;
        this.zone = zone;
        this.rating = rating;
        this.brand = brand;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.serialNo = serialNo;
        this.meterNo = meterNo;
        this.year = year;
        this.time = time;
        this.category = category;
        this.imageUri = imageUri;
    }

    public Transformer(String name, String location, String voltageRatio, String feeder, String zone, String rating, String brand, Double latitude, Double longitude, String status, String serialNo, String meterNo, String year, String time, String category, String imageUri) {
        this.name = name;
        this.location = location;
        this.voltageRatio = voltageRatio;
        this.feeder = feeder;
        this.zone = zone;
        this.rating = rating;
        this.brand = brand;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.serialNo = serialNo;
        this.meterNo = meterNo;
        this.year = year;
        this.time = time;
        this.category = category;
        this.imageUri = imageUri;
    }


    protected Transformer(Parcel in) {
        uid = in.readString();
        name = in.readString();
        location = in.readString();
        voltageRatio = in.readString();
        feeder = in.readString();
        zone = in.readString();
        rating = in.readString();
        brand = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        status = in.readString();
        serialNo = in.readString();
        meterNo = in.readString();
        year = in.readString();
        time = in.readString();
        category = in.readString();
        imageUri = in.readString();
    }

    public static final Creator<Transformer> CREATOR = new Creator<Transformer>() {
        @Override
        public Transformer createFromParcel(Parcel in) {
            return new Transformer(in);
        }

        @Override
        public Transformer[] newArray(int size) {
            return new Transformer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(voltageRatio);
        dest.writeString(feeder);
        dest.writeString(zone);
        dest.writeString(rating);
        dest.writeString(brand);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(status);
        dest.writeString(serialNo);
        dest.writeString(meterNo);
        dest.writeString(year);
        dest.writeString(time);
        dest.writeString(category);
        dest.writeString(imageUri);
    }
}

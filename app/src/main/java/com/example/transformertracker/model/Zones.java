package com.example.transformertracker.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Zones implements Parcelable {

    public String name;
    public String amount;
    public String recentUpdate;

    public Zones() {
    }

    public Zones(String name, String amount, String recentUpdate) {
        this.name = name;
        this.amount = amount;
        this.recentUpdate = recentUpdate;
    }

    protected Zones(Parcel in) {
        name = in.readString();
        amount = in.readString();
        recentUpdate = in.readString();
    }

    public static final Creator<Zones> CREATOR = new Creator<Zones>() {
        @Override
        public Zones createFromParcel(Parcel in) {
            return new Zones(in);
        }

        @Override
        public Zones[] newArray(int size) {
            return new Zones[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(amount);
        dest.writeString(recentUpdate);
    }
}

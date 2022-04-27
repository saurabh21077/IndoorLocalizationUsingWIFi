package com.mc2022.template.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WardriveWifiLocationTable")
public class WardriveWifiLocation {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "WifiName")
    private String wifiName;

    @ColumnInfo(name = "RSSI")
    private String RSSI;

    @ColumnInfo(name = "LocationName")
    private String name;

    public WardriveWifiLocation(String wifiName, String RSSI, String name) {
        this.wifiName = wifiName;
        this.RSSI = RSSI;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getRSSI() {
        return RSSI;
    }

    public void setRSSI(String RSSI) {
        this.RSSI = RSSI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

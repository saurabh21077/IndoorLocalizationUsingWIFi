package com.mc2022.template.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mc2022.template.model.WifiData;

import java.util.List;

@Dao
public interface WifiDataDAO {
    // list all
    @Query("Select * from WifiDataTable")
    List<WifiData> getList();

    // Insert
    @Insert
    void insert(WifiData wifiData);

//    @Query("Select WifiTable.WifiName from WifiTable where id = 1")
//    String getConnectWifi();

    @Query("SELECT * FROM (SELECT * FROM WifiDataTable ORDER BY RSSIValue DESC) ORDER BY id ASC")
    List<WifiData> getWifiDataInDescOrder();

    // Delete using id
    @Query("DELETE from WifiDataTable where id = :id")
    void deleteUsingID(int id);

    // Delete using object
    @Delete
    void delete(WifiData wifiData);
}

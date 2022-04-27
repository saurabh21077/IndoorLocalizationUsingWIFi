package com.mc2022.template.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mc2022.template.model.UserLocation;
import com.mc2022.template.model.WardriveWifiLocation;

import java.util.List;

@Dao
public interface WardriveWifiLocationDAO {

    //List all
    @Query("Select * from WardriveWifiLocationTable")
    List<WardriveWifiLocation> getList();

    // Insert
    @Insert
    void insert(WardriveWifiLocation wardriveWifiLocation);

    // Delete using id
    @Query("DELETE from WardriveWifiLocationTable where id = :id")
    void deleteUsingID(int id);

    // Delete using object
    @Delete
    void delete(WardriveWifiLocation wardriveWifiLocation);
}

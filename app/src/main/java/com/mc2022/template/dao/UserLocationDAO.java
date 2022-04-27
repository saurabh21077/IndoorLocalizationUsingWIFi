package com.mc2022.template.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mc2022.template.model.UserLocation;

import java.util.List;

@Dao
public interface UserLocationDAO {

    //List all
    @Query("Select * from UserLocationTable")
    List<UserLocation> getList();

    // Insert
    @Insert
    void insert(UserLocation userLocation);

    // Delete using id
    @Query("DELETE from UserLocationTable where id = :id")
    void deleteUsingID(int id);

    // Delete using object
    @Delete
    void delete(UserLocation userLocation);
}

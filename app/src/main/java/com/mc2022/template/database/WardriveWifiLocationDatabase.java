package com.mc2022.template.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mc2022.template.dao.UserLocationDAO;
import com.mc2022.template.dao.WardriveWifiLocationDAO;
import com.mc2022.template.model.WardriveWifiLocation;

@Database(entities = {WardriveWifiLocation.class}, version = 2)
public abstract class WardriveWifiLocationDatabase extends RoomDatabase {
    public abstract WardriveWifiLocationDAO wardriveWifiLocationDAO();

    // instance of database
    // we should have single instance of database and should ensure that our database class should be singleton

    public static WardriveWifiLocationDatabase WardriveWifiLocationInstance;

    public static WardriveWifiLocationDatabase getInstance(Context context){

        if(WardriveWifiLocationInstance == null){
            WardriveWifiLocationInstance = Room.databaseBuilder(context.getApplicationContext(), WardriveWifiLocationDatabase.class, "WardriveWifiLocation_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }

        return WardriveWifiLocationInstance;
    }
}

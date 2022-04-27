package com.mc2022.template.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mc2022.template.dao.UserLocationDAO;
import com.mc2022.template.dao.WifiDataDAO;
import com.mc2022.template.model.WifiData;

@Database(entities = {WifiData.class}, version = 2)
public abstract class WifiDataDatabase extends RoomDatabase {
    public abstract WifiDataDAO wifiDataDAO();

    // instance of database
    // we should have single instance of database and should ensure that our database class should be singleton

    public static WifiDataDatabase wifiDataInstance;

    public static WifiDataDatabase getInstance(Context context){

        if(wifiDataInstance == null){
            wifiDataInstance = Room.databaseBuilder(context.getApplicationContext(), WifiDataDatabase.class, "WifiData_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }

        return wifiDataInstance;
    }
}

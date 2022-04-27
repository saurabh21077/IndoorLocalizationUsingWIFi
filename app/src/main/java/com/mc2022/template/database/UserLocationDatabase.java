package com.mc2022.template.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mc2022.template.dao.UserLocationDAO;
import com.mc2022.template.model.UserLocation;

@Database(entities = {UserLocation.class}, version = 2)
public abstract class UserLocationDatabase extends RoomDatabase {
    public abstract UserLocationDAO userLocationDAO();

    // instance of database
    // we should have single instance of database and should ensure that our database class should be singleton

    public static UserLocationDatabase userLocationInstance;

    public static UserLocationDatabase getInstance(Context context){

        if(userLocationInstance == null){
            userLocationInstance = Room.databaseBuilder(context.getApplicationContext(), UserLocationDatabase.class, "location_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }

        return userLocationInstance;
    }
}

package com.example.supertimer;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import com.example.supertimer.data.ActionDao;
import com.example.supertimer.data.AppDatabase;
import com.example.supertimer.data.TimerDao;

public class App extends Application {
    private AppDatabase database;
    private TimerDao timerDao;
    private ActionDao actionDao;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db-name")
                .allowMainThreadQueries()
                .build();

        timerDao = database.timerDao();
        actionDao = database.actionDao();

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public TimerDao getTimerDao() {
        return timerDao;
    }

    public void setTimerDao(TimerDao timerDao) {
        this.timerDao = timerDao;
    }

    public ActionDao getActionDao() {
        return actionDao;
    }

    public void setActionDao(ActionDao actionDao) {
        this.actionDao = actionDao;
    }
}

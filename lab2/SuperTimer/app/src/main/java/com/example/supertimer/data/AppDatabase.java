package com.example.supertimer.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.supertimer.model.Action;
import com.example.supertimer.model.Timer;

@Database(entities = {Action.class, Timer.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TimerDao timerDao();
    public abstract ActionDao actionDao();
}

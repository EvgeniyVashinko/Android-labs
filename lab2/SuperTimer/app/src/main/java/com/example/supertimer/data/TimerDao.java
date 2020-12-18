package com.example.supertimer.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.supertimer.model.Timer;

import java.util.List;

@Dao
public interface TimerDao {
    @Query("SELECT * FROM timer")
    List<Timer> getAllTimers();

    @Query("SELECT * FROM timer")
    LiveData<List<Timer>> getAllTimersLiveData();

    @Query("SELECT * FROM timer WHERE id LIKE :timerId LIMIT 1")
    Timer findById(int timerId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Timer timer);

    @Update
    void update(Timer timer);

    @Delete
    void delete(Timer timer);
}

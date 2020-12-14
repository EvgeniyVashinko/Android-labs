package com.example.supertimer.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.supertimer.model.Action;

import java.util.List;

@Dao
public interface ActionDao {
    @Query("SELECT * FROM `action`")
    List<Action> getAllActions();

    @Query("SELECT * FROM `Action`")
    LiveData<List<Action>> getAllActionsLiveData();

    @Query("SELECT * FROM `ACTION` WHERE timer_id = :timerId")
    List<Action> findActionsByTimerId(int timerId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Action action);

    @Update
    void update(Action action);

    @Delete
    void delete(Action action);
}

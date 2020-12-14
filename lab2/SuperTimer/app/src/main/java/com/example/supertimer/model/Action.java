package com.example.supertimer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Timer.class, parentColumns = "id", childColumns = "timer_id", onDelete = CASCADE))
public class Action {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "seconds_num")
    public int seconds;
    @ColumnInfo(name = "timer_id")
    public int tid;

    public Action() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return id == action.id &&
                seconds == action.seconds &&
                tid == action.tid &&
                Objects.equals(name, action.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, seconds, tid);
    }
}

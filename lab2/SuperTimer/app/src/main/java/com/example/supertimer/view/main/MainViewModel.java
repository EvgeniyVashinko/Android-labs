package com.example.supertimer.view.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.supertimer.App;
import com.example.supertimer.model.Timer;

import java.util.List;

public class MainViewModel extends ViewModel {
    private LiveData<List<Timer>> timerLiveData = App.getInstance().getTimerDao().getAllTimersLiveData();

    public LiveData<List<Timer>> getTimerLiveData() {
        return timerLiveData;
    }
}

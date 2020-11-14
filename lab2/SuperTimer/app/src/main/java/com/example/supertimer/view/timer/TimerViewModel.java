package com.example.supertimer.view.timer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.List;

public class TimerViewModel extends ViewModel {
    private MutableLiveData<Integer> position;
    private MutableLiveData<Integer> time;
//    private List<Action> actionList;
    private boolean serviceStart = false;


    public LiveData<Integer> getPosition() {
        if (position == null) {
            position = new MutableLiveData<Integer>();
        }
        return position;
    }

    public void setPosition(int position) {
        if (this.position == null) {
            this.position = new MutableLiveData<Integer>(position);
        }
        this.position.setValue(position);
    }

    public LiveData<Integer> getTime() {
        if (time == null) {
            time = new MutableLiveData<Integer>();
        }
        return time;
    }

    public void setTime(int time) {
        if (this.time == null) {
            this.time = new MutableLiveData<Integer>(time);
        }
        this.time.setValue(time);
    }

    public boolean isServiceStart() {
        return serviceStart;
    }

    public void setServiceStart(boolean serviceStart) {
        this.serviceStart = serviceStart;
    }

//    public void setActionList(List<Action> actionList) {
//        this.actionList = actionList;
//    }

}

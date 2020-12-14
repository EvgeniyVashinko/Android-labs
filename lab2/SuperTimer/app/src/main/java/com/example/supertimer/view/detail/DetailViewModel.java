package com.example.supertimer.view.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.supertimer.App;
import com.example.supertimer.model.Action;
import com.example.supertimer.model.Timer;

import java.util.List;

public class DetailViewModel extends ViewModel {
    private LiveData<List<Action>> actionLiveData = App.getInstance().getActionDao().getAllActionsLiveData();

    public LiveData<List<Action>> getActionLiveData() {
        return actionLiveData;
    }
}

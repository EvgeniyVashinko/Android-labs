package com.example.supertimer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.supertimer.model.Action;

import java.util.List;


public class TimerService extends Service {
    private MediaPlayer player;
    private Vibrator vibrator;
    private List<Action> actionList;
    private CountDownTimer t;
    private int position;
    private Action action;
    private int timerId;
    private int time;
    MyBinder binder = new MyBinder();
    private boolean timerState = true; //true when timer work

    public void setPosition(int position) {
        this.position = position;
        action = actionList.get(position);
        StartTimer(action.seconds);
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setTimerId(int timerId) {
        this.timerId = timerId;
    }

    //передавать в интент бандл из чего-то там
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        player = MediaPlayer.create(this, R.raw.musend);
//        player.setLooping(true); // зацикливаем
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        player.stop();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        timerId = intent.getExtras().getInt("timerId", -1);
        position = intent.getExtras().getInt("position", 0);
        actionList = App.getInstance().getActionDao().findActionsByTimerId(timerId);
        action = actionList.get(position);
        StartTimer(action.seconds);
//        player.start();
    }

    private void StartTimer(int sec){
        if(t != null){
            t.cancel();
        }
        t =
        new CountDownTimer(sec * 1000, 1000) {
            @Override
            public void onTick(long l) {
                //Передаем в главное активити значение тика для его последующего отображения на экране
                time = (int)(l/1000);
                //передавать значение секунд в активити
                Intent intent = new Intent("TIMER_ACTION");
                intent.putExtra("currentTime", time);
                intent.putExtra("actionName", action.name);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                if (position < actionList.size() - 1){
                    Toast.makeText(getApplicationContext(), action.name+" завершено!",Toast.LENGTH_SHORT).show();
                    setPosition(position + 1);
                    //подумать
//                    long mills = 1000L;
//                    if (vibrator.hasVibrator()) {
//                        vibrator.vibrate(mills);
//                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Таймер завершен!",Toast.LENGTH_SHORT).show();
//                    long mills = 3000L;
//                    if (vibrator.hasVibrator()) {
//                        vibrator.vibrate(mills);
//                    }
                }

                player.start();
            }
        };
//        setTime(sec);
        //передавать значение секунд в активити
        t.start();
    }

    public void TimerPause(){
        if (timerState){//if timer work
            if(t != null){
                t.cancel();
            }
        }
        else{
            StartTimer(time);
        }
        timerState = !timerState;
    }

    public class MyBinder extends Binder{
        public TimerService getService(){
            return TimerService.this;
        }
    }

}


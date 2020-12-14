package com.example.supertimer.view.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.supertimer.App;
import com.example.supertimer.R;
import com.example.supertimer.model.Timer;
import com.example.supertimer.view.detail.DetailActivity;
import com.example.supertimer.view.timer.TimerActivity;

import java.util.List;

public class TimerAdapter extends ArrayAdapter<Timer> {

    private LayoutInflater inflater;
    private int layout;
    private List<Timer> timers;
    private Context context;
    private int size;

    public TimerAdapter(Context context, int resource, List<Timer> timers) {
        super(context, resource, timers);
        this.context = context;
        this.timers = timers;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.size = 12;
    }

    public void setSize(int num){
        size = num;
    }
    public int getSize() { return size; }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView timerName = (TextView)view.findViewById(R.id.timerTextView);
        timerName.setTextSize(size);
        View start = view.findViewById(R.id.startTimer);
        View edit = view.findViewById(R.id.editTimer);
        View delete = view.findViewById(R.id.deleteTimer);

        Timer timer = timers.get(position);

        timerName.setText(timer.name);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TimerActivity.class);
                intent.putExtra("id", timer.id);
                context.startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", timer.id);
                context.startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getInstance().getTimerDao().delete(timer);
            }
        });

        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linLayout);
        linearLayout.setBackgroundColor(timer.color);

        return view;
    }
}

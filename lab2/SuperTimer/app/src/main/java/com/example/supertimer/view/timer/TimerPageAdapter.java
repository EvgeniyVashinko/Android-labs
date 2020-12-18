package com.example.supertimer.view.timer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.supertimer.App;
import com.example.supertimer.R;
import com.example.supertimer.model.Action;

import java.util.List;

public class TimerPageAdapter extends ArrayAdapter<Action> {
    private LayoutInflater inflater;
    private int layout;
    private List<Action> actions;

    public TimerPageAdapter(Context context, int resource, List<Action> actions) {
        super(context, resource, actions);
        this.actions = actions;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView actionName = (TextView)view.findViewById(R.id.actTextView);
        TextView seconds = (TextView)view.findViewById(R.id.actSecTextView);

        Action action = actions.get(position);

        actionName.setText(action.name);
        seconds.setText(Integer.toString(action.seconds));

        return view;
    }
}

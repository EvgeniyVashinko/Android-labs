package com.example.supertimer.view.detail;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.supertimer.model.Timer;

import java.util.List;

public class ActionAdapter  extends ArrayAdapter<Action> {

    private LayoutInflater inflater;
    private int layout;
    private List<Action> actions;

    public ActionAdapter(Context context, int resource, List<Action> actions) {
        super(context, resource, actions);
        this.actions = actions;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        EditText actionName = (EditText) view.findViewById(R.id.nameEditText);
        View edit = view.findViewById(R.id.editAction);
        View delete = view.findViewById(R.id.deleteAction);
        Button plus = (Button)view.findViewById(R.id.plusButton);
        Button minus = (Button)view.findViewById(R.id.minusButton);
        TextView seconds = (TextView)view.findViewById(R.id.secondsTextView);

        Action action = actions.get(position);

        actionName.setText(action.name);
        seconds.setText(Integer.toString(action.seconds));

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action.seconds+=1;
                App.getInstance().getActionDao().update(action);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action.seconds-=1;
                App.getInstance().getActionDao().update(action);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Переходим на страницу редактирования действия
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getInstance().getActionDao().delete(action);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action.name = actionName.getText().toString();
                App.getInstance().getActionDao().update(action);
            }
        });

        return view;
    }

}

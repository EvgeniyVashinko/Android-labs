package com.example.seabattle.Statistics;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.seabattle.R;

import java.util.List;

public class StatAdapter extends ArrayAdapter<GameStat>{
    private LayoutInflater inflater;
    private int layout;
    private List<GameStat> stats;

    public StatAdapter(Context context, int resource, List<GameStat> actions) {
        super(context, resource, actions);
        this.stats = actions;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView result = (TextView) view.findViewById(R.id.game_result);
        TextView time = (TextView) view.findViewById(R.id.game_time);
        TextView score = (TextView) view.findViewById(R.id.game_score);

        GameStat gs = stats.get(position);

        result.setText(gs.getResult());
        if (gs.getResult().equals("Победа")){
            result.setTextColor(Color.GREEN);
        }
        else{
            result.setTextColor(Color.RED);
        }
        time.setText(gs.getTime());
        score.setText(gs.getScore());

        return view;
    }
}

package com.example.seabattle.Statistics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seabattle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private List<GameStat> gameStatArrayList;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("statistics").child(mAuth.getUid());
        lv = (ListView) findViewById(R.id.stat_lv);

        gameStatArrayList = new ArrayList<GameStat>();

        ChildEventListener gamesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GameStat gameStat = new GameStat();
                gameStat = snapshot.getValue(GameStat.class);
                gameStat.setTime(snapshot.getKey());
                gameStatArrayList.add(gameStat);
//                Toast.makeText(StatisticsActivity.this, gameStat.getTime(), Toast.LENGTH_SHORT).show();
                updateUI();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef.addChildEventListener(gamesListener);
    }
    private void updateUI(){
        StatAdapter adapter = new StatAdapter(getApplicationContext(), R.layout.gamestat_item,gameStatArrayList);
        lv.setAdapter(adapter);
    }
}
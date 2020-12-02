package com.example.seabattle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.seabattle.Field.Cell;
import com.example.seabattle.Field.CellState;
import com.example.seabattle.Field.FieldMode;
import com.example.seabattle.Field.GameField;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GameActivity extends AppCompatActivity {

    GameField myField, enemyField;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String enemyId, playerId;
    DatabaseReference statisticsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        myField = (GameField) findViewById(R.id.my_field);
        enemyField = (GameField) findViewById(R.id.enemy_field);


        myField.setMode(FieldMode.MyField);
        enemyField.setMode(FieldMode.Inactive);

        Bundle bundle = getIntent().getExtras();
        String code = bundle.getString("code");

        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("games").child(code);
        statisticsRef = database.getReference("statistics");

        enemyField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enemyField.getMode() != FieldMode.Inactive) {
                    Cell cell;
                    cell = enemyField.getLastClickCell();
                    String key = String.valueOf(cell.getX()) + String.valueOf(cell.getY());
                    String value = cell.getCellState().toString();
                    if (!enemyField.CellWasMiss()){
                        myRef.child(enemyId).child(key).setValue(value);
                    }
                }
            }
        });


        ChildEventListener enemyFieldListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // проинициализировать поле врага
                String key = snapshot.getKey().toString();
                int i = Integer.parseInt(String.valueOf(key.charAt(0)));
                int j = Integer.parseInt(String.valueOf(key.charAt(1)));
                enemyField.setFieldCell(i,j, CellState.valueOf(snapshot.getValue().toString()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // действие на изменение
                String key = snapshot.getKey().toString();
                int i = Integer.parseInt(String.valueOf(key.charAt(0)));
                int j = Integer.parseInt(String.valueOf(key.charAt(1)));
                enemyField.setFieldCell(i,j, CellState.valueOf(snapshot.getValue().toString()));
                if (!snapshot.getValue().toString().equals(CellState.Hit.toString())){
                    myRef.child("CurrentPlayer").setValue(enemyId);
                }
                if (enemyField.CheckWin() || myField.CheckWin()){
                    myRef.child("CurrentPlayer").setValue("Stop");
                }

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

        ChildEventListener playerFieldListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // проинициализировать свое поле
                String key = snapshot.getKey().toString();
                int i = Integer.parseInt(String.valueOf(key.charAt(0)));
                int j = Integer.parseInt(String.valueOf(key.charAt(1)));
                myField.setFieldCell(i,j, CellState.valueOf(snapshot.getValue().toString()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // действие на изменение
                String key = snapshot.getKey().toString();
                int i = Integer.parseInt(String.valueOf(key.charAt(0)));
                int j = Integer.parseInt(String.valueOf(key.charAt(1)));
                myField.setFieldCell(i,j, CellState.valueOf(snapshot.getValue().toString()));
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

        ValueEventListener currentPlayerListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().equals(mAuth.getUid())){
                    enemyField.setMode(FieldMode.EnemyField);
                    Toast.makeText(GameActivity.this, "Ваш ход", Toast.LENGTH_SHORT).show();

                }
                else{
                    enemyField.setMode(FieldMode.Inactive);
                }

                if (snapshot.getValue().toString().equals("Stop")){
                    enemyField.setMode(FieldMode.Show);
                    Toast.makeText(GameActivity.this, "Игра окончена", Toast.LENGTH_SHORT).show();
                    if (enemyField.CheckWin()){
                        Date currentDate = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss-dd-MM-yyyy", Locale.getDefault());
                        String dateText = dateFormat.format(currentDate);
                        statisticsRef = statisticsRef.child(mAuth.getUid()).child(dateText);
                        statisticsRef.child("result").setValue("Победа");
                        statisticsRef.child("player1").setValue(playerId);
                        statisticsRef.child("player2").setValue(enemyId);
                        String score = String.valueOf(enemyField.getDestroyedShipNum()) + " - " +  String.valueOf(myField.getDestroyedShipNum());
                        statisticsRef.child("score").setValue(score);
                    }
                    else {
                        Date currentDate = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss-dd-MM-yyyy", Locale.getDefault());
                        String dateText = dateFormat.format(currentDate);
                        statisticsRef = statisticsRef.child(mAuth.getUid()).child(dateText);
                        statisticsRef.child("result").setValue("Поражение");
                        statisticsRef.child("player1").setValue(playerId);
                        statisticsRef.child("player2").setValue(enemyId);
                        String score = String.valueOf(enemyField.getDestroyedShipNum()) + " - " + String.valueOf(myField.getDestroyedShipNum());
                        statisticsRef.child("score").setValue(score);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String k = snapshot.getKey();
                if (!k.equals("CurrentPlayer")){
                    if (k.equals(mAuth.getUid())){
                        playerId = k;
                        myRef.child(playerId).addChildEventListener(playerFieldListener);
                    }else {
                        enemyId = k;
                        myRef.child(enemyId).addChildEventListener(enemyFieldListener);
                    }
                }

                if (playerId != null && enemyId != null){
                    myRef.child("CurrentPlayer").addValueEventListener(currentPlayerListener);
                }
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

        myRef.addChildEventListener(childEventListener);
    }




    private void GetFieldFromDbData(){
//        String player1 = myRef.
    }
}
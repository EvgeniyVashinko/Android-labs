package com.example.seabattle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.seabattle.Field.Cell;
import com.example.seabattle.Field.CellState;
import com.example.seabattle.Field.FieldMode;
import com.example.seabattle.Field.GameField;
import com.example.seabattle.Models.AppTheme;
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
    ChildEventListener childEventListener;
    ChildEventListener enemyFieldListener;
    ChildEventListener playerFieldListener;
    ValueEventListener currentPlayerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTheme();
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


        enemyFieldListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // проинициализировать поле врага
                SetCellState(snapshot.getKey(), CellState.valueOf(snapshot.getValue().toString()), enemyField);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // действие на изменение
                SetCellState(snapshot.getKey(), CellState.valueOf(snapshot.getValue().toString()), enemyField);

                if (enemyField.CheckWin() || myField.CheckWin()){
                    myRef.child("CurrentPlayer").setValue("Stop");
                }

                if (!snapshot.getValue().toString().equals(CellState.Hit.toString())){
                    myRef.child("CurrentPlayer").setValue(enemyId);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "enFL  - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        playerFieldListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // проинициализировать свое поле
                SetCellState(snapshot.getKey(), CellState.valueOf(snapshot.getValue().toString()), myField);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // действие на изменение
                SetCellState(snapshot.getKey(), CellState.valueOf(snapshot.getValue().toString()), myField);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "plFL  - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        currentPlayerListener = new ValueEventListener() {
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
                        WriteStat("Победа");
                    }
                    else if (myField.CheckWin()){
                        WriteStat("Поражение");
                    }

                    myRef.child(playerId).removeEventListener(playerFieldListener);
                    myRef.child(enemyId).removeEventListener(enemyFieldListener);
                    myRef.child("CurrentPlayer").removeEventListener(currentPlayerListener);
                    myRef.removeEventListener(childEventListener);
                    myRef.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "CurPL  - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        childEventListener = new ChildEventListener() {
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
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "chEL  - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        myRef.addChildEventListener(childEventListener);
    }

    @Override
    public void onBackPressed() {
        myRef.child("CurrentPlayer").setValue("Stop");
        myRef.removeValue();
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
//        super.onBackPressed();
    }

    private void WriteStat(String result){
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss-dd-MM-yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        statisticsRef = statisticsRef.child(mAuth.getUid()).child(dateText);
        statisticsRef.child("result").setValue(result);
        statisticsRef.child("player1").setValue(playerId);
        statisticsRef.child("player2").setValue(enemyId);
        String score = String.valueOf(enemyField.getDestroyedShipNum()) + " - " +  String.valueOf(myField.getDestroyedShipNum());
        statisticsRef.child("score").setValue(score);
    }

    private void SetCellState(String key, CellState cellState, GameField field){
        int i = Integer.parseInt(String.valueOf(key.charAt(0)));
        int j = Integer.parseInt(String.valueOf(key.charAt(1)));
        field.setFieldCell(i,j, cellState);
    }

    private void SetTheme(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String themeVal = sp.getString("theme", "Classic");
        AppTheme appTheme = AppTheme.valueOf(themeVal);
        switch (appTheme){
            case LightPink:
                setTheme(R.style.Light_pink_theme);
                break;
            case DarkPink:
                setTheme(R.style.Dark_pink_theme);
                break;
            case Dark:
                setTheme(R.style.Theme_AppCompat_NoActionBar);
                break;
            case Classic:
                setTheme(R.style.Theme_SeaBattle);
                break;
        }
    }
}
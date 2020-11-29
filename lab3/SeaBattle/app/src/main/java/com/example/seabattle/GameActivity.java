package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.seabattle.Field.FieldMode;
import com.example.seabattle.Field.GameField;

public class GameActivity extends AppCompatActivity {

    GameField myField, enemyField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myField = (GameField) findViewById(R.id.my_field);
        enemyField = (GameField) findViewById(R.id.enemy_field);

        myField.setMode(FieldMode.MyField);
        enemyField.setMode(FieldMode.EnemyField);
    }
}
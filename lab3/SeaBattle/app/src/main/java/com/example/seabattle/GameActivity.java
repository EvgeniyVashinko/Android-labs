package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.seabattle.Field.Cell;
import com.example.seabattle.Field.CellState;
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

        enemyField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cell cell;
                cell = enemyField.getLastClickCell();
                String str = cell.getX() + "_" + cell.getY() + "_" + cell.getCellState();
                String str2 = cell.getCellState().toString();
                CellState cs = CellState.valueOf(str2);
                enemyField.setFieldCell(9,1,cs);
                Toast.makeText(getApplicationContext(), cs.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
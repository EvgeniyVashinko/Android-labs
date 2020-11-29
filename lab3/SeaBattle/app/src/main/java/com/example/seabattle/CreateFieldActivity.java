package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seabattle.Field.FieldMode;
import com.example.seabattle.Field.GameField;
import com.example.seabattle.Field.ShipPositionVerifier;

public class CreateFieldActivity extends AppCompatActivity {

    GameField createField;
    EditText code;
    Button create, connect;
    ShipPositionVerifier spv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_field);

        createField = (GameField) findViewById(R.id.create);
        createField.setMode(FieldMode.CreateField);

        create = (Button) findViewById(R.id.create_game);
        connect = (Button) findViewById(R.id.connect);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spv = new ShipPositionVerifier(createField.getField());
                if (spv.Check()){
                    Toast.makeText(getApplicationContext(), "Good!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Bad!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
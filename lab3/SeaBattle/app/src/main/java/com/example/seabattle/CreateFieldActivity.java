package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seabattle.Field.CellState;
import com.example.seabattle.Field.FieldMode;
import com.example.seabattle.Field.GameField;
import com.example.seabattle.Field.ShipPositionVerifier;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateFieldActivity extends AppCompatActivity {

    GameField createField;
    EditText code;
    Button create, connect;
    ShipPositionVerifier spv;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    GameMode gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_field);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        createField = (GameField) findViewById(R.id.create);
        createField.setMode(FieldMode.CreateField);

        create = (Button) findViewById(R.id.create_game);
        connect = (Button) findViewById(R.id.connect);
        code = (EditText) findViewById(R.id.code);

        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        gameMode = GameMode.valueOf(bundle.get("mode").toString());

        if (gameMode == GameMode.Connect.Create){
            code.setText(mAuth.getUid());
        }
        else {
            code.setHint("Введите код игры");
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spv = new ShipPositionVerifier(createField.getField());
                if (spv.Check()){
                    Toast.makeText(getApplicationContext(), "Good!", Toast.LENGTH_SHORT).show();
                    AddFieldToDB(createField.getField());
                    Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                    intent.putExtra("code", "GAME_" + code.getText().toString());
                    startActivity(intent);
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

    private void AddFieldToDB(CellState[][] field){
        String gameId = "GAME_" + code.getText().toString();
        myRef = database.getReference("games").child(gameId);

        String key, value;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                key = String.valueOf(i) + String.valueOf(j);
                value = String.valueOf(field[i][j]);
                myRef.child(mAuth.getUid()).child(key).setValue(value);
            }
        }
        myRef.child("CurrentPlayer").setValue(code.getText().toString());
    }

}
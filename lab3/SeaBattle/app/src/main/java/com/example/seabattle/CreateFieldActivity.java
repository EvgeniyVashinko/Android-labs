package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.seabattle.Models.AppTheme;
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
    String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTheme();
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

        if (gameMode == GameMode.Create){
            code.setText(mAuth.getUid());
            gameId = "GAME_" + code.getText().toString();
            myRef = database.getReference("games").child(gameId);
            myRef.removeValue();
        }
        else {
            code.setHint("Введите код игры");
            create.setText("Подключиться");
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
//        String gameId = "GAME_" + code.getText().toString();
//        myRef = database.getReference("games").child(gameId);
        gameId = "GAME_" + code.getText().toString();
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
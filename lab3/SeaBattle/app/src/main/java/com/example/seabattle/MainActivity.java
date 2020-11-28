package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends AppCompatActivity {

    int fieldLength = 10, i, j;
    String qq = "0";
    Button buttons[][] = new Button[fieldLength][fieldLength];
    GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        gridLayout = (GridLayout) findViewById(R.id.gameTable);
//        for (i = 0; i < fieldLength; i++){
//
//            TableRow tableRow = new TableRow(this);
//
//            for (j = 0; j < fieldLength; j++){
//
//                Button button = new Button(this);
//                button.setText("*");
//                buttons[i][j] = button;
//                String position = String.valueOf(i) + String.valueOf(j);
//                button.setId(Integer.parseInt(position));
//
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Button button = (Button) view;
//                        Toast.makeText(getApplicationContext(), String.valueOf(button.getId()), Toast.LENGTH_SHORT).show();
//                        button.setText(qq);
//                        if (qq.equals("1")){
//                            qq = "0";
//                        }else{
//                            qq = "1";
//                        }
//
//                    }
//                });
//                tableRow.addView(button, j);
//            }
//
//            gridLayout.addView(tableRow, i);
//        }
    }
}
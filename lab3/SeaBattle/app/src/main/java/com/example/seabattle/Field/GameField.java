package com.example.seabattle.Field;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.seabattle.R;

import java.util.ArrayList;

public class GameField extends View {

    int fieldLength = 10;
    CellState[][] field;
    Context context;
    int cellHeight, cellWidth;
    FieldMode mode;

    public GameField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.fieldLength = 10;
        MakeField(fieldLength);
        mode = FieldMode.EnemyField;
    }

    private void MakeField(int fieldLength){
        field = new CellState[fieldLength][fieldLength];
        for (int i = 0; i < fieldLength; i++){
            for (int j = 0; j < fieldLength; j++){
                field[i][j] = CellState.Empty;
            }
        }
//        field[1][2] = CellState.Hit;
//        field[8][9] = CellState.Hit;
//        field[3][4] = CellState.Miss;
//        field[4][3] = CellState.Miss;
//        field[5][6] = CellState.Ship;
//        field[5][7] = CellState.Ship;
        field[1][2] = CellState.Ship;
        field[8][9] = CellState.Ship;
        field[3][4] = CellState.Ship;
        field[4][3] = CellState.Ship;
        field[5][6] = CellState.Ship;
        field[5][7] = CellState.Ship;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellHeight = getHeight() / fieldLength;
        cellWidth = getWidth() / fieldLength;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        DrawFieldBorder(canvas);
        DrawCellsBorder(canvas);

        for (int i = 0; i < fieldLength; i++) {
            for (int j = 0; j < fieldLength; j++) {
                switch (field[i][j]){
                    case Hit:
                        DrawHitCell(canvas, i, j);
                        break;
                    case Miss:
                        if (mode == FieldMode.EnemyField){
                            DrawMissCell(canvas, i, j);
                        }
                        break;
                    case Ship:
                        if (mode == FieldMode.CreateField || mode == FieldMode.MyField){
                            DrawShipCell(canvas, i, j);
                        }
                        break;
                }
            }
        }
    }

    private void DrawFieldBorder(Canvas canvas){
        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStrokeWidth(20);
        borderPaint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(0,getHeight(),getWidth(),0, borderPaint);
    }

    private void DrawCellsBorder(Canvas canvas) {
        Paint cellBorderPaint = new Paint();
        cellBorderPaint.setColor(Color.BLACK);
        cellBorderPaint.setStrokeWidth(10);

        for (int i = 1; i < fieldLength; i++) {
            canvas.drawLine(i*cellWidth, 0, i*cellWidth, getHeight(), cellBorderPaint);
            canvas.drawLine(0, i*cellWidth, getHeight(), i*cellWidth, cellBorderPaint);
        }
    }

    private void DrawHitCell(Canvas canvas, int i, int j){
        Paint hitPaint = new Paint();
        hitPaint.setColor(Color.RED);
        hitPaint.setStrokeWidth(10);

        canvas.drawLine(j * cellWidth, i * cellHeight,(j + 1) * cellWidth,(i + 1) * cellHeight, hitPaint);
        canvas.drawLine((j+1) * cellWidth, i * cellHeight,j * cellHeight,(i + 1) * cellWidth, hitPaint);
    }

    private void DrawShipCell(Canvas canvas, int i, int j){
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.light_blue));
        paint.setStrokeWidth(10);

        canvas.drawRect(j * cellWidth, i * cellHeight, (j+1)* cellWidth, (i+1)*cellHeight, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(j * cellWidth, i * cellHeight, (j+1)* cellWidth, (i+1)*cellHeight, paint);
    }

    private void DrawMissCell(Canvas canvas, int i, int j){
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(25);

        canvas.drawPoint(j * cellWidth + cellWidth/2, i * cellHeight + cellHeight/2, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            int j = (int) (event.getX() / cellHeight);
            int i = (int) (event.getY() / cellWidth);
            switch (mode){
                case MyField:
                    break;
                case EnemyField:
                    if (field[i][j] == CellState.Ship){
                        field[i][j] = CellState.Hit;
                        if (CheckShipDestruction(i,j)){
                            DrawMissAroundDestroyedShip();
                        }
                    } else if (field[i][j] == CellState.Empty){
                        field[i][j] = CellState.Miss;
                    }
                    break;
                case CreateField:
                    if (field[i][j] != CellState.Ship){
                        field[i][j] = CellState.Ship;
                    } else {
                        field[i][j] = CellState.Empty;
                    }
                    break;
            }

            invalidate();
        }
        return true;
    }

    ArrayList<CellPosition> ship = new ArrayList<>();

    private boolean CheckShipDestruction(int i, int j){

//        if (IsNotExistOrNotShip(i-1, j) && IsNotExistOrNotShip(i+1, j)  // значит он уничтожен одинарный
//                && IsNotExistOrNotShip(i, j-1) && IsNotExistOrNotShip(i, j+1)){
//            ship.add(new CellPosition(i,j));
//            return true;
//        }
        if (IsNotExistOrNotShip(i-1, j) && IsNotExistOrNotShip(i+1, j)){ // по горизонтали
            while (j >= 0){
                if (field[i][j] == CellState.Ship){
                    ship.clear();
                    return false;
                }
                if (field[i][j] == CellState.Hit){
                    ship.add(new CellPosition(i,j));
                }
                else{
                    break;
                }
                j--;
            }
            if (ship.size() == 4){
                return true;
            }
            j = ship.get(0).getJ();
            while (j <= 9){
                if (field[i][j] == CellState.Ship){
                    ship.clear();
                    return false;
                }
                if (field[i][j] == CellState.Hit){
                    ship.add(new CellPosition(i,j));
                }
                else{
                    break;
                }
                j++;
            }
        } else { // по вертикали
            while (i >= 0){
                if (field[i][j] == CellState.Ship){
                    ship.clear();
                    return false;
                }
                if (field[i][j] == CellState.Hit){
                    ship.add(new CellPosition(i,j));
                }
                else{
                    break;
                }
                i--;
            }
            if (ship.size() == 4){
                return true;
            }
            i = ship.get(0).getI();
            while (i <= 9){
                if (field[i][j] == CellState.Ship){
                    ship.clear();
                    return false;
                }
                if (field[i][j] == CellState.Hit){
                    ship.add(new CellPosition(i,j));
                }
                else{
                    break;
                }
                i++;
            }
        }
        return true;
    }

    private void DrawMissAroundDestroyedShip(){
        CellPosition cell;
        for (int i = 0; i < ship.size(); i++) {
            cell = ship.get(i);

            int _i = cell.getI();
            int _j = cell.getJ();

            for (int j = -1; j <= 1 ; j++) {

                for (int k = -1; k <= 1 ; k++) {

                    if (IsExists(_i - j, _j - k)){
                        if (field[_i - j][_j - k] == CellState.Empty){
                            field[_i - j][_j - k] = CellState.Miss;
                        }
                    }
                }
            }
        }
        invalidate();
    }

    private boolean IsNotExistOrNotShip(int i, int j){
        boolean result;
        try {
            result = field[i][j] != CellState.Ship;
        }
        catch (Exception e) {
            result = true;
        }
        return result;
    }

    private boolean IsExists(int i, int j){
        boolean result;
        try {
            result = field[i][j] == field[i][j];
        }
        catch (Exception e){
            result = false;
        }
        return result;
    }
}

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

public class GameField extends View {

    int fieldLength = 10;
    CellState[][] field;
    Context context;
    int cellHeight, cellWidth;

    public GameField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.fieldLength = 10;
        MakeField(fieldLength);
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
                        DrawMissCell(canvas, i, j);
                        break;
                    case Ship:
                        DrawShipCell(canvas, i, j);
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
            field[i][j] = CellState.Hit;
            invalidate();
        }
        return true;
    }
}

package com.example.seabattle.Field;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.seabattle.Models.AppTheme;
import com.example.seabattle.R;

import java.util.ArrayList;

public class GameField extends View {

    int fieldLength = 10;
    CellState[][] field;
    Context context;
    int cellHeight, cellWidth;
    FieldMode mode;
    Cell cell;
    private int destroyedShipNum;
    private boolean wasMiss = false;
    Paint borderPaint;
    Paint cellBorderPaint;
    Paint hitPaint;
    Paint missCellPaint;
    Paint shipPaint;
    Paint shipBorderPaint;
    int fieldColor;
    private AppTheme appTheme = AppTheme.Classic;


    public GameField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.fieldLength = 10;
        MakeField(fieldLength);
        mode = FieldMode.EnemyField;
        destroyedShipNum = 0;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String themeVal = sp.getString("theme", "Classic");
        appTheme = AppTheme.valueOf(themeVal);

        switch (appTheme){
            case Dark:
                DarkThemePaints();
                fieldColor = getResources().getColor(R.color.darkGray);
                break;
            case DarkPink:
                PinkDarkThemePaints();
                fieldColor = getResources().getColor(R.color.darkGray);
                break;
            case LightPink:
                PinkLightThemePaints();
                fieldColor = Color.WHITE;
                break;
            default:
                ClassicThemePaints();
                fieldColor = Color.WHITE;
        }
    }

    private void PaintsInitialization(int fieldBorderColor, int cellBorderColor, int missCellColor, int shipColor, int shipBorderColor, int hitColor){
        borderPaint = new Paint();
        borderPaint.setColor(fieldBorderColor);
        borderPaint.setStrokeWidth(20);
        borderPaint.setStyle(Paint.Style.STROKE);

        cellBorderPaint = new Paint();
        cellBorderPaint.setColor(cellBorderColor);
        cellBorderPaint.setStrokeWidth(10);

        missCellPaint = new Paint();
        missCellPaint.setColor(missCellColor);
        missCellPaint.setStrokeWidth(25);

        shipPaint = new Paint();
        shipPaint.setColor(shipColor);
        shipPaint.setStrokeWidth(10);

        shipBorderPaint = new Paint();
        shipBorderPaint.setColor(shipBorderColor);
        shipBorderPaint.setStrokeWidth(10);
        shipBorderPaint.setStyle(Paint.Style.STROKE);

        hitPaint = new Paint();
        hitPaint.setColor(hitColor);
        hitPaint.setStrokeWidth(10);
    }
    private void DarkThemePaints(){
        PaintsInitialization(Color.GRAY, Color.GRAY, Color.GRAY, getResources().getColor(R.color.light_blue), Color.BLUE, Color.RED);
    }

    private void ClassicThemePaints(){
        PaintsInitialization(Color.BLACK, Color.BLACK, Color.GRAY, getResources().getColor(R.color.light_blue), Color.BLUE, Color.RED);
    }

    private void PinkLightThemePaints(){
        PaintsInitialization(getResources().getColor(R.color.light_pink), getResources().getColor(R.color.light_pink), Color.GRAY, getResources().getColor(R.color.light_pink), getResources().getColor(R.color.pink), Color.BLUE);
    }

    private void PinkDarkThemePaints(){
        PaintsInitialization(getResources().getColor(R.color.light_pink), getResources().getColor(R.color.light_pink), Color.GRAY, getResources().getColor(R.color.light_pink), getResources().getColor(R.color.pink), Color.BLUE);
    }

    private void MakeField(int fieldLength){
        field = new CellState[fieldLength][fieldLength];
        for (int i = 0; i < fieldLength; i++){
            for (int j = 0; j < fieldLength; j++){
                field[i][j] = CellState.Empty;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellHeight = getHeight() / fieldLength;
        cellWidth = getWidth() / fieldLength;
        invalidate();
    }

    public void setMode(FieldMode mode) {
        this.mode = mode;
        invalidate();
    }

    public int getDestroyedShipNum() {
        destroyedShipNum = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j] == CellState.Hit){
                    destroyedShipNum += 1;
                }
            }
        }
        return destroyedShipNum;
    }

    public FieldMode getMode() {
        return mode;
    }

    public void setField(CellState[][] field) {
        this.field = field;
        invalidate();
    }

    public CellState[][] getField() {
        return field;
    }

    public void setFieldCell(int i, int j, CellState cell) {
        this.field[i][j] = cell;
        invalidate();
    }

    public boolean CellWasMiss(){
        return this.wasMiss;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(fieldColor);

        DrawCellsBorder(canvas);
        DrawFieldBorder(canvas);

        for (int i = 0; i < fieldLength; i++) {
            for (int j = 0; j < fieldLength; j++) {
                switch (field[i][j]){
                    case Hit:
                        DrawHitCell(canvas, i, j);
                        break;
                    case Miss:
                        if (mode == FieldMode.EnemyField || mode == FieldMode.MyField || mode == FieldMode.Inactive || mode == FieldMode.Show){
                            DrawMissCell(canvas, i, j);
                        }
                        break;
                    case Ship:
                        if (mode == FieldMode.CreateField || mode == FieldMode.MyField || mode == FieldMode.Show){
                            DrawShipCell(canvas, i, j);
                        }
                        break;
                }
            }
        }
    }


    private void DrawFieldBorder(Canvas canvas){
        if (mode == FieldMode.Inactive){
            borderPaint.setColor(Color.RED);
        }else if(mode == FieldMode.EnemyField){
            borderPaint.setColor(Color.GREEN);
        }
        canvas.drawRect(0,getHeight(),getWidth(),0, borderPaint);
    }

    private void DrawCellsBorder(Canvas canvas) {
        for (int i = 1; i < fieldLength; i++) {
            canvas.drawLine(i*cellWidth, 0, i*cellWidth, getHeight(), cellBorderPaint);
            canvas.drawLine(0, i*cellWidth, getHeight(), i*cellWidth, cellBorderPaint);
        }
    }

    private void DrawHitCell(Canvas canvas, int i, int j){
        canvas.drawLine(j * cellWidth, i * cellHeight,(j + 1) * cellWidth,(i + 1) * cellHeight, hitPaint);
        canvas.drawLine((j+1) * cellWidth, i * cellHeight,j * cellHeight,(i + 1) * cellWidth, hitPaint);
    }

    private void DrawShipCell(Canvas canvas, int i, int j){
        canvas.drawRect(j * cellWidth, i * cellHeight, (j+1)* cellWidth, (i+1)*cellHeight, shipPaint);

        canvas.drawRect(j * cellWidth, i * cellHeight, (j+1)* cellWidth, (i+1)*cellHeight, shipBorderPaint);
    }

    private void DrawMissCell(Canvas canvas, int i, int j){
        canvas.drawPoint(j * cellWidth + cellWidth/2, i * cellHeight + cellHeight/2, missCellPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            int j = (int) (event.getX() / cellHeight);
            int i = (int) (event.getY() / cellWidth);
            if (i < 0){
                i = 0;
            }
            if (i > 9){
                i = 9;
            }
            if (j < 0){
                j = 0;
            }
            if (j > 9){
                j = 9;
            }
            switch (mode){
                case MyField:
                case Inactive:
                    break;
                case EnemyField:
                    wasMiss = field[i][j] == CellState.Miss;
                    if (field[i][j] == CellState.Ship){
//                        destroyedShipNum += 1;
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

            cell = new Cell(i, j, field[i][j]);
            invalidate();
        }
        return true;
    }

    public Cell getLastClickCell(){
        return cell;
    }

    private OnClickListener onClickListener;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_UP &&
                (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if(onClickListener != null) onClickListener.onClick(this);
        }
        return super.dispatchKeyEvent(event);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setPressed(true);
        }
        else if(event.getAction() == MotionEvent.ACTION_UP) {
            if(onClickListener != null) onClickListener.onClick(this);
            setPressed(false);
        }
        else {
            setPressed(false);
        }
        return super.dispatchTouchEvent(event);
    }
    @Override
    public void setOnClickListener(OnClickListener l) {
        onClickListener = l;
    }



    ArrayList<CellPosition> ship = new ArrayList<>();

    private boolean CheckShipDestruction(int i, int j){
        ship.clear();

        if (IsShipOrHit(i, j - 1) || IsShipOrHit(i, j + 1)){ // по горизонтали

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

            j = ship.get(0).getJ() + 1;
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

            i = ship.get(0).getI() + 1;
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

//    public boolean CheckWin(){
//        return this.destroyedShipNum == 20;
//    }

    public boolean CheckWin(){
        int shipNum = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j] == CellState.Hit){
                    shipNum += 1;
                }
            }
        }
        return shipNum == 20;
    }

    private boolean IsShipOrHit(int i, int j){
        boolean result;
        try {
            result = field[i][j] == CellState.Ship || field[i][j] == CellState.Hit;
        }
        catch (Exception e) {
            result = false;
        }
        return result;
    }
}

package com.example.seabattle.Field;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShipDestructionСhecker {
    private CellState field[][];
    private String playerId, enemyId, gameCode;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<CellPosition> ship = new ArrayList<>();

    public ShipDestructionСhecker(CellState[][] field, String playerId, String enemyId, String gameCode) {
        this.field = field;
        this.playerId = playerId;
        this.gameCode = gameCode;
        this.enemyId = enemyId;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("games").child(gameCode);
    }



    public boolean CheckShipDestruction(int i, int j){
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

    public void DrawMissAroundDestroyedShip(){
        CellPosition cell;
        myRef.child("CurrentPlayer").setValue("Check");
        for (int i = 0; i < ship.size(); i++) {
            cell = ship.get(i);

            int _i = cell.getI();
            int _j = cell.getJ();

            for (int j = -1; j <= 1 ; j++) {

                for (int k = -1; k <= 1 ; k++) {

                    if (IsExists(_i - j, _j - k)){
                        if (field[_i - j][_j - k] == CellState.Empty){
//                            field[_i - j][_j - k] = CellState.Miss;

                            String key = String.valueOf(_i - j) + String.valueOf(_j - k);
                            myRef.child(enemyId).child(key).setValue(CellState.Miss.toString());
                        }
                    }
                }
            }
        }
        myRef.child("CurrentPlayer").setValue(playerId);
    }

    public void DrawMissIfShipDestroyed(int i, int j){
        if (CheckShipDestruction(i,j)){
            DrawMissAroundDestroyedShip();
        }
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

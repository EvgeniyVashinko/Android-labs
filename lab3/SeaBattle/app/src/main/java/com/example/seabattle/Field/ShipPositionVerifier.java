package com.example.seabattle.Field;

public class ShipPositionVerifier {
    private CellState field[][];
    private boolean checkedCells[][];
    int size;

    public ShipPositionVerifier(CellState[][] field, boolean[][] checkedCells) {
        this.field = field;
        this.checkedCells = checkedCells;
        size = field.length;
    }

    public ShipPositionVerifier(CellState[][] field) {
        this.field = field;
        size = field.length;
        checkedCells = new boolean[size][size];
    }

    public boolean Check(){

        if (!CheckShipCellsNum()){
            return false;
        }

        return CheckShipNum();
    }

    private boolean CheckShipCellsNum(){
        int shipCellsNum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == CellState.Ship){
                    shipCellsNum += 1;
                }
            }
        }
        return shipCellsNum == 20;
    }

    private boolean CheckShipCellsInAngles(int i, int j) {
        boolean a = IsNotExistOrNotShip(i - 1, j - 1);
        boolean b = IsNotExistOrNotShip(i - 1, j + 1);
        boolean c = IsNotExistOrNotShip(i + 1, j - 1);
        boolean d = IsNotExistOrNotShip(i + 1, j + 1);
        return !IsNotExistOrNotShip(i - 1, j - 1) || !IsNotExistOrNotShip(i - 1, j + 1)
                || !IsNotExistOrNotShip(i + 1, j - 1) || !IsNotExistOrNotShip(i + 1, j + 1);
    }

    private boolean CheckShipNum(){
        int one = 0, two = 0, three = 0, four = 0;
        int shipLen = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {



                if (field[i][j] == CellState.Ship){

                    if (CheckShipCellsInAngles(i,j)){
                        return false;
                    }

                    if (checkedCells[i][j]){
                        continue;
                    }
                    if (!IsNotExistOrNotShip(i, j - 1) || !IsNotExistOrNotShip(i, j + 1)){//по горизонтали
                        int x = j, y = i;
//                        while (x >= 0){
//                            x--;
//                        }
//                        x = j + 1;
                        while (x <= 9){
                            if (field[i][x] == CellState.Ship){
                                checkedCells[i][x] = true;
                                shipLen += 1;
                            }
                            else{
                                break;
                            }
                            x++;
                        }
                    }
                    else{// по вертикали
                        int x = j, y = i;

                        while (y <= 9){
                            if (field[y][j] == CellState.Ship){
                                checkedCells[y][j] = true;
                                shipLen += 1;
                            }
                            else{
                                break;
                            }
                            y++;
                        }
                    }

                    switch (shipLen){
                        case 1:
                            one++;
                            break;
                        case 2:
                            two++;
                            break;
                        case 3:
                            three++;
                            break;
                        case 4:
                            four++;
                            break;
                        default:
                            return false;
                    }
                    shipLen = 0;
                }
            }
        }
        return one == 4 && two == 3 && three == 2 && four == 1;
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

//    private boolean IsShipOrNotExist(int i, int j){
//        boolean result;
//        try {
//            result = field[i][j] == CellState.Ship;
//        }
//        catch (Exception e) {
//            result = true;
//        }
//        return result;
//    }
}

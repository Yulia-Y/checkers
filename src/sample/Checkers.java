package sample;

import java.util.ArrayList;

public class Checkers {

    static final int
            EMPTY = 0,
            WHITE = 1,
            WHITE_QUEEN = 2,
            BLACK = 3,
            BLACK_QUEEN = 4; // обознаение разных видов шашек в массиве
    public static boolean flag = false; // хранит информацию о том, рубили ли прошлым ходом
     int[][] board; // массив шашек

    Checkers() {
        board = new int[8][8];
        setUpGame();
    }

     void setUpGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ( row % 2 != col % 2 ) {
                    if (row < 3)
                        board[row][col] = BLACK;
                    else if (row > 4)
                        board[row][col] = WHITE;
                    else
                        board[row][col] = EMPTY;
                }
                else {
                    board[row][col] = EMPTY;
                }
            }
        }
    } // начальная расстановка шашек по доске

    int pieceAt(int row, int col) {
        return board[row][col];
    } // возвращает шашку в заданной ячейке


    void makeMove(CheckersMove move ) {
        board[move.toRow][move.toCol] = board[move.fromRow][move.fromCol]; // записывает шашку в клетку, куда она сходила
        board[move.fromRow][move.fromCol] = EMPTY; // опустошает ту, где она стояла
        if (flag) {
            board[move.jumpRow][move.jumpCol] = EMPTY;
        } // если была срублена шашка, она удаляется с доски
        if (move.toRow == 0 && board[move.toRow][move.toCol] == WHITE)
            board[move.toRow][move.toCol] = WHITE_QUEEN; // если достигнут край доски шашка становится дамкой
        if (move.toRow == 7 && board[move.toRow][move.toCol] == BLACK)
            board[move.toRow][move.toCol] = BLACK_QUEEN; // если достигнут край доски шашка становится дамкой
    }


    CheckersMove[] getLegalMoves(int player) { // функция находит и возвращает возможные ходы

        if (player != WHITE && player != BLACK)
            return null;

        int playerKing;
        if (player == WHITE)
            playerKing = WHITE_QUEEN;
        else
            playerKing = BLACK_QUEEN;

        ArrayList<CheckersMove> moves = new ArrayList<>();

       // сначала ищем может ли игрок рубить т.к. он обязан это сделать при возможности
        for (int row = 0; row < 8; row++) { // проходим по всему массиву с шашками и проверяем каждую на возможность рубить
            for (int col = 0; col < 8; col++) {
                moves.addAll(GetJumps(player, playerKing, row, col));
            }
        }
        flag = true;


        if (moves.size() == 0) { // если ранее не были найдены ходы
            flag = false; // флаг обнуляется т.к игрок точно не будет рубить
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == player) { // ищем обычные ходы для шашкек и дамок
                        if (canMove( row, col, row + 1, col + 1))
                            moves.add(new CheckersMove(row, col, row + 1, col + 1));
                        if (canMove( row, col, row - 1, col + 1))
                            moves.add(new CheckersMove(row, col, row - 1, col + 1));
                        if (canMove( row, col, row + 1, col - 1))
                            moves.add(new CheckersMove(row, col, row + 1, col - 1));
                        if (canMove( row, col, row - 1, col - 1))
                            moves.add(new CheckersMove(row, col, row - 1, col - 1));
                    }
                    if (board[row][col] == playerKing) {
                            moves.addAll(KingCanMove( row, col,   1,1));
                            moves.addAll(KingCanMove(row, col,  -1,1));
                            moves.addAll(KingCanMove( row, col,  1,-1));
                            moves.addAll(KingCanMove( row, col, -1,-1));

                    }
                }
            }
        }

        if (moves.size() == 0)
            return null; // если ходов вообще не найдено возвращаем нуль
        else {
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray; // в противном случае переписываем ходы из списка в массив и возвращаем его
        }
    }

    ArrayList<CheckersMove> GetJumps(int player, int playerKing, int row, int col){

        ArrayList<CheckersMove> moves = new ArrayList<>();
        if (board[row][col] == player) { // если это обычная шашка проверяем 4 клетки вокруг
            if (canJump( row, col, row+1, col+1, row+2, col+2))
                moves.add(new CheckersMove(row, col, row+2, col+2, row+1, col+1));
            if (canJump( row, col, row-1, col+1, row-2, col+2))
                moves.add(new CheckersMove(row, col, row-2, col+2, row-1, col+1));
            if (canJump( row, col, row+1, col-1, row+2, col-2))
                moves.add(new CheckersMove(row, col, row+2, col-2,row+1, col-1));
            if (canJump( row, col, row-1, col-1, row-2, col-2))
                moves.add(new CheckersMove(row, col, row-2, col-2, row-1, col-1));
        }
        if ( board[row][col] == playerKing) { // для дамки проверяем диагонали во всех направлениях
            moves.addAll(KingCanJump(playerKing, row, col,   1, 1));
            moves.addAll(KingCanJump(playerKing, row, col,  -1, 1));
            moves.addAll(KingCanJump(playerKing, row, col,   1, -1));
            moves.addAll(KingCanJump(playerKing, row, col,  -1, -1));
        }
        return moves;
    }

   CheckersMove[] getLegalJumpsFrom(int player, int row, int col) { // при повторном ходе игрока (если он срубил шашку) функция проверяет возможность рубить для конкретной шашки
        if (player != WHITE && player != BLACK)
            return null;
        int playerKing;
        if (player == WHITE)
            playerKing = WHITE_QUEEN;
        else
            playerKing = BLACK_QUEEN;

       ArrayList<CheckersMove> moves = GetJumps(player, playerKing, row, col);
        if (moves.size() == 0){
            return null;} // если ходы не найдены возвращаем нуль
        else {
            flag = true; // фиксируем, что игрок будет рубить
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray; // если ходы найдены переписываем их в массив и возвращаем
        }
    }



    private boolean canJump( int r1, int c1, int r2, int c2, int r3, int c3) {
    // r1,c1 - шашка, которой ходят, r2,c2 - шашка, которую хотят срубить, r3,c3 - клетка куда ставят шашку
        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
            return false; // если клетка за пределами массива возвращаем false

        if (board[r3][c3] != EMPTY)
            return false; // если клетка занята возвращаем false

        if (board[r1][c1] == WHITE) {
            return board[r2][c2] == BLACK || board[r2][c2] == BLACK_QUEEN;
        }
        if (board[r1][c1] == BLACK) {
            return board[r2][c2] == WHITE || board[r2][c2] == WHITE_QUEEN;
        } // если игрок пытается срубить шашку противника, возвращаем true
        return false;
    }



    private boolean canMove( int r1, int c1, int r2, int c2) {
        // r1,c1 - шашка, которой ходят, r2,c2 - клетка куда ставят шашку
        if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
            return false; // если клетка за пределами массива возвращаем false
        if (board[r2][c2] != EMPTY)
            return false; // если клетка занята возвращаем false

        if (board[r1][c1] == WHITE) {
            return  r2 <= r1;
        }
        else {
            return  r2 >= r1; // если шашка может ходить в данном направлении (вверх/вниз) возвращаем true
        }

    }

    private boolean IsEmpty(int r, int c){
        if (r<8 && r>-1 && c<8 && c>-1){
            return board[r][c] == EMPTY;
        }  // проверяет сначала попадание в массив, а потом пуста ли клетка
        return false;
    }

    private ArrayList<CheckersMove> KingCanMove(int r1, int c1, int k1, int k2){ // ищет возможные ходы для дамок
        // r1,c1 - шашка, которой ходят, k1,k2 - коэффициенты, задающие направление по диагонали
        ArrayList<CheckersMove> Moves = new ArrayList<>();

            int k = 1; // создаем счетчик
            while ( IsEmpty(r1+k1*k,c1+k2*k)) { // пока клетка пуста, добавляем ходы в список
                Moves.add(new CheckersMove(r1, c1, r1+k1*k, c1+k2*k));
                k++;
            }
       return Moves; }


private boolean IsKill (int player, int r, int c){
// player - игрок,  r,c - параметры проверяемой клетки
        if (player==WHITE_QUEEN){
            if(r<8 && r>-1 && c<8 && c>-1){ // проверяем попадание в массив
                if (board[r][c]==BLACK || board[r][c] == BLACK_QUEEN){ // если найдена шашка противника
                    return true;  // возвращаем true
                }
            }
        }
    if (player==BLACK_QUEEN){
        if(r<8 && r>-1 && c<8 && c>-1){
            return board[r][c] == WHITE || board[r][c] == WHITE_QUEEN;
        }
    }
       return false;
}

    private ArrayList<CheckersMove> KingCanJump(int player, int r1, int c1, int k1, int k2) {
        // r1,c1 - шашка, которой ходят, k1,k2 - коэффициенты, задающие направление по диагонали
        ArrayList<CheckersMove> Moves = new ArrayList<>();

            if(IsKill(player, r1+k1, c1+k2)){ // проверяем клетку соседнюю с той, которой ходит игрок, можно ли ее срубить
                int t = 1; // создаем счетчик
                while (IsEmpty(r1+k1*(1+t), c1+k2*(1+t))){  // находим все клетки, куда можно поставить шашку
                    Moves.add(new CheckersMove(r1, c1, r1+k1*(1+t), c1+k2*(1+t), r1+k1, c1+k2));
                    t++;
                }
            }
            int k = 1;
            while (IsEmpty(r1+k1*k, c1+k2*k)){ // идем по пустм клеткам, пока не найдешь шашку, которую можно срубить
                if(IsKill(player, r1+k1*(k+1), c1+k2*(k+1))){ // если таковая найдена
                    int t = 1;
                    while (IsEmpty(r1+k1*(k+1+t), c1+k2*(k+1+t))){ // находим все клетки, куда можно поставить шашку
                        Moves.add(new CheckersMove(r1, c1, r1+k1*(k+1+t), c1+k2*(k+1+t), r1+k1*(k+1), c1+k2*(k+1)));
                        t++;
                    }
                }
                k++;
            }
        return Moves;  // возвращаем возможне ход
    }
}

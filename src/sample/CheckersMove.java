package sample;



public class CheckersMove {
    int fromRow, fromCol;  // Положение фигуры, подлежащей перемещению.
    int toRow, toCol;   // Квадрат, к которому нужно двигаться.
    int jumpRow, jumpCol; //Положение фигуры, которую рубят
    CheckersMove(int r1, int c1, int r2, int c2) {
        fromRow = r1;
        fromCol = c1;
        toRow = r2;
        toCol = c2;
    }

    CheckersMove(int r1, int c1, int r2, int c2, int r3, int c3) {
        this(r1,c1,r2,c2);
        jumpRow = r3;
        jumpCol = c3;
    }
}


package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static sample.Checkers.flag; // переменная, показывающая рубили ли шашки прошлым ходом и нужно ли дать возможность рубить дальше (да=true, нет=false)

public class Board  {
    Image BoardImage = new Image(new FileInputStream("src/sample/Board.jpg"));
    Image WhiteChecker = new Image(new FileInputStream("src/sample/1_41.png"));
    Image BlackChecker = new Image(new FileInputStream("src/sample/2_4.png"));
    Image WhiteQueen = new Image(new FileInputStream("src/sample/crown.png"));
    Image BlackQueen = new Image(new FileInputStream("src/sample/BlackQ.png"));
    Image Choice = new Image(new FileInputStream("src/sample/minimize.png"));
    Image Square = new Image(new FileInputStream("src/sample/fullscreen.png"));
    // загрузка всех используемых изображений

    Checkers board; // создание массива с шашками и начальная расстановка шашек

    boolean gameInProgress; // переменная, отвечающая за состояние игры (игра идет = true, игра закончена = false)
    int currentPlayer; // показвает игра, чей сейчас ход (WHITE = 1,BLACK = 3)
    int selectedRow, selectedCol; // переменные, хранящие столбец и строку массива, соответствующие выбранной клетке

    CheckersMove[] legalMoves; // массив для ходов


    Board() throws FileNotFoundException {
        board = new Checkers();
        doNewGame();
    }


    void doNewGame() {
        if (gameInProgress) {
           Main.text.setText("Сначала завершите игру!");
           return;
       }
        board.setUpGame();
        currentPlayer = Checkers.WHITE;
        legalMoves = board.getLegalMoves(Checkers.WHITE); // получаем возможные ходы для белых
        selectedRow = -1; // показывает, что клетка еще не выбрана
        Main.text.setText("Ход белых");
        gameInProgress = true;
        CreateBoard();
    } // функция возвращает массив с шашками в начальное состояние, передает ход белым и рисует новую доску

        void GiveUp() {
        if (!gameInProgress) {
            Main.text.setText("Игра еще не начата!");
            return;
        }
        if (currentPlayer == Checkers.WHITE)
           gameOver("Белые сдались. Черные победили!");
        else
            gameOver("Черные сдались. Белые победили!");
    }

    void gameOver(String str) {
        Main.text.setText(str);
        gameInProgress = false;
    } // останавливает игру и выводит соответствующее сообщение об ее окончании


    void doClickSquare(int row, int col) {

        for (CheckersMove legalMove : legalMoves)
            if (legalMove.fromRow == row && legalMove.fromCol == col){
                selectedRow = row;
                selectedCol = col;
                if (currentPlayer == Checkers.WHITE)
                    Main.text.setText("Ход белых.");
                else
                    Main.text.setText("Ход черных.");
                CreateBoard();
                return;
            } // сравниваем координаты выбранной клетки со списком активных шашек
        // при нахождении соответствия записываем строку и столбец выбранной шашки

        if (selectedRow < 0) {
            Main.text.setText("Выберите шашку, которой хотите сделать ход.");
            return;
        } // если шашка не выбрана, просим выбрать


        for (CheckersMove legalMove : legalMoves)
            if (legalMove.fromRow == selectedRow && legalMove.fromCol == selectedCol
                    && legalMove.toRow == row && legalMove.toCol == col) {
                doMakeMove(legalMove);
                return;
            } // после выбора шашки при нажатии сравниваются координаты выбранной клетки с возможными ходами для ранее выбранной шашки
        // если таких не найдено просим выбрать клетку для хода
        Main.text.setText("Выберите клетку, куда хотите сходить.");
    }

    void doMakeMove(CheckersMove move) {

        board.makeMove(move); // изменение положение шашек в массиве в соответсвии с ходом

        if (flag) { // если прошлым ходом игрок срубил шашку
            flag = false;
            legalMoves = board.getLegalJumpsFrom(currentPlayer,move.toRow,move.toCol); // проверяем может ли шашка рубить дальше
            // и получаем соответствующие ходы
            if (legalMoves != null) { // если ходы найдены игрока просят рубить
                if (currentPlayer == Checkers.WHITE)
                   Main.text.setText("Белые должны продолжить рубить.");
                else
                    Main.text.setText("Черные должны продолжить рубить.");
                selectedRow = move.toRow;
                selectedCol = move.toCol; // в качестве выбранной шашки автоматически записываем ту, которой ходили до этого
                CreateBoard(); // обновляем экран доску
                return;
            }
        }

        if (currentPlayer == Checkers.WHITE) {
            currentPlayer = Checkers.BLACK; // передаем ход другому игроку
            legalMoves = board.getLegalMoves(currentPlayer); // получаем ходы для текущего игрока
            if (legalMoves == null) // если у игрока нет ходов в его очередь, игра заканчивается
               gameOver("У черных закончились ходы. Победа за белыми!");
            else if (flag) // если у игрока есть возможность рубить, его просят рубить
                Main.text.setText("Черные должны рубить.");
            else
                Main.text.setText("Ход черных.");
        }
        else {
            currentPlayer = Checkers.WHITE; // передаем ход другому игроку
            legalMoves = board.getLegalMoves(currentPlayer); // получаем ходы для текущего игрока
            if (legalMoves == null) // если у игрока нет ходов в его очередь, игра заканчивается
                gameOver("У белых закончились ходы. Победа за черными!");
            else if (flag) // если у игрока есть возможность рубить, его просят рубить
               Main.text.setText("Белые должны рубить.");
            else
               Main.text.setText("Ход белых.");
        }

        selectedRow = -1; //  'обнуляем' выбранную шашку

        if (legalMoves != null) {
            boolean sameStartSquare = true;
            for (int i = 1; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow != legalMoves[0].fromRow
                        || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                    sameStartSquare = false;
                    break;
                } // если все возможные ходы совершаются одной шашкой
            if (sameStartSquare) {
                selectedRow = legalMoves[0].fromRow;
                selectedCol = legalMoves[0].fromCol;
            }
        } // то она выбирается автоматически
        CreateBoard(); // обновление экрана

    }

 public void PlaceImage(Image Checker, int row, int col){
     ImageView place = new ImageView(Checker);
     place.setFitWidth(78);
     place.setFitHeight(78);
     place.setX(38+col*78);
     place.setY(36+row*78);
     Main.root.getChildren().add(place);
 } // Функция размещает изображение шашки, возможного хода или рамки вокруг активной шашки

    public void CreateBoard(){

        ImageView Board = new ImageView(BoardImage);
        Board.setFitHeight(697);
        Board.setFitWidth(703);
        Main.root.getChildren().add(Board);  // Вывод изображения доски

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                switch (board.pieceAt(row,col)) {
                    case Checkers.WHITE:
                        PlaceImage(WhiteChecker,row, col);
                        break;
                    case Checkers.BLACK:
                        PlaceImage(BlackChecker,row, col);
                        break;
                    case Checkers.WHITE_QUEEN:
                        PlaceImage(WhiteQueen,row, col);
                        break;
                    case Checkers.BLACK_QUEEN:
                        PlaceImage(BlackQueen,row, col);
                        break;
                    default:
                        break;
                }
            }
        } // размещение шашек на экране, согласно их расположению в массиве

        if (gameInProgress) {
            // Обвести фигуры, которые можно перемещать
            for (CheckersMove legalMove : legalMoves) {
                PlaceImage(Square, legalMove.fromRow, legalMove.fromCol);
            }
        }
               /* Если фигура выбрана для перемещения (т.е. если selectedRow >= 0), то
               разместить указатели на каждую клетку, на которую можно переместить эту фигуру. */
            if (selectedRow >= 0) {
                for (CheckersMove legalMove : legalMoves) {
                    if (legalMove.fromCol == selectedCol && legalMove.fromRow == selectedRow) {
                        PlaceImage(Choice, legalMove.toRow, legalMove.toCol);
                    }
                }
            }
        }

}


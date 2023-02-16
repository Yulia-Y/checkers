package sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Board  {
    Image BoardImage = new Image(new FileInputStream("src/sample/Board.jpg"));
    Image WhiteChecker = new Image(new FileInputStream("src/sample/White.png"));
    Image BlackChecker = new Image(new FileInputStream("src/sample/Black.png"));
    Image WhiteQueen = new Image(new FileInputStream("src/sample/WQ.jpg"));
    Image BlackQueen = new Image(new FileInputStream("src/sample/BQ.jpg"));
    Image Star = new Image(new FileInputStream("src/sample/koi.png"));
    Image Square = new Image(new FileInputStream("src/sample/fullscreen.png"));
    ImageView[][] checkers = new ImageView[8][8];
    CheckersData board;  // Здесь хранятся данные для шахматной доски.
    // Эта board также отвечает за генерацию
    // спискок законных ходов.

    boolean gameInProgress; // Идет ли игра в данный момент?

    /* Следующие три переменные действительны только в процессе игры. */

    int currentPlayer;      // Чья теперь очередь?  Возможные значения:
    //    are CheckersData.RED and CheckersData.BLACK.

    int selectedRow, selectedCol;  // Если текущий игрок выбрал фигуру для
    //     движения, они дают строку и столбец
    //     содержащий этот кусок.  Если никакой выбранной части нет, выбранная строка равна -1.

    CheckersMove[] legalMoves;  // Массив, содержащий законные ходы для
    //   текущего игрока


    //JLabel message;  // Лейбл для отображения сообщений пользователю.

    Board() throws FileNotFoundException {



       /* message = new JLabel("",JLabel.CENTER);
        message.setFont(new  Font("Serif", Font.BOLD, 18));
        message.setForeground(Color.BLACK);*/
        board = new CheckersData();
        doNewGame();
    }





    /**
     * Начните новую игру
     */
    void doNewGame() {
       // if (gameInProgress == true) {
            // Это не должно быть возможно, но проверить не помешает.
           // message.setText("Finish the current game first!");
          //  return;
      //  }
        board.setUpGame();   // Расставьте фигуры.
        currentPlayer = CheckersData.WHITE;   // КРАСНЫЙ движется первым.
        legalMoves = board.getLegalMoves(CheckersData.WHITE);  // Получить возможные ходы красных
        selectedRow = -1;   // КРАСНЫЙ еще не выбрал фигуру для перемещения.
       // message.setText("Red:  Make your move.");
        gameInProgress = true;
       // NewGame.setEnabled(false);
       // Resign.setEnabled(true);

        CreateBoard();
    }


    /**
     * Текущий игрок сдается. Игра заканчивается. Противник побеждает.
     */
        void doResign() {
        if (gameInProgress == false) {  // Должно быть, это невозможно.
            //message.setText("There is no game in progress!");
            return;
        }
        if (currentPlayer == CheckersData.WHITE);
           // gameOver("RED resigns.  BLACK wins.");
        else;
            //gameOver("BLACK resigns.  RED wins.");
    }


    /**
     * Игра заканчивается.  Параметр str отображается в виде сообщения
     * для пользователя.  Состояния кнопок настраиваются таким образом, чтобы игроки
     * могли начать новую игру.  Этот метод вызывается при игре
     * заканчивается в любой точке этого класса.
     */
    /*void gameOver(String str) {
        //message.setText(str);
        MaNewGame.setEnabled(true);
        Resign.setEnabled(false);
        gameInProgress = false;
    }


    /**
     * Это вызывается mousePressed (), когда игрок нажимает на
     * квадрат в указанной строке и колонке.  Здесь уже проверено
     * что игра, по сути, идет.
     */
    void doClickSquare(int row, int col) {

         /* Если игрок нажал на одну из фигур, то игрок
          можно переместить, отметить эту строку и столбец как выбранные и вернуться.  (Это
          может изменить предыдущий выбор.)  Сбросьте сообщение,
          если оно ранее отображало сообщение об ошибке. */

        for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                selectedRow = row;
                selectedCol = col;
                //if (currentPlayer == CheckersData.WHITE);
                    //message.setText("RED:  Make your move.");
               // else
                   // message.setText("BLACK:  Make your move.");


                CreateBoard();
                return;
            }

         /* Если ни одна фигура не была выбрана для перемещения, пользователь должен сначала
          выбирать кусок.  Покажите сообщение об ошибке и вернитесь. */

        if (selectedRow < 0) {
            //message.setText("Click the piece you want to move.");
            return;
        }

         /* Если пользователь нажал на квадрат, где выбранная фигура может быть
            законно перемещена, то сделайте ход и вернитесь. */

        for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol
                    && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
                doMakeMove(legalMoves[i]);
                return;
            }

         /* Если мы дойдем до этой точки, то там будет выбран кусок, а квадрат, где
          пользователь просто нажал не тот, где этот кусок может быть легально перемещен.
          Показать сообщение об ошибке. P.S. нажал туда, куда не может ходить*/

        //message.setText("Click the square you want to move to.");

    }  // конец doClickSquare()


    /**
     * Это вызывается, когда текущий игрок выбрал указанное
     * движение. Сделайте ход, а затем либо закончите, либо продолжите игру
     *  соответствующим образом.
     */
    void doMakeMove(CheckersMove move) {

        board.makeMove(move);

         /* Если ход был прыжком, то вполне возможно, что у игрока есть еще один
          с убийством.  Проверьте наличие законных прыжков, начиная с квадрата, который игрок
          только что переехал.  Если они есть, игрок должен прыгнуть.  Тот же самый
          игрок продолжает движение.
          */

        if (move.isJump()) {
            legalMoves = board.getLegalJumpsFrom(currentPlayer,move.toRow,move.toCol);
            if (legalMoves != null) {
                if (currentPlayer == CheckersData.WHITE);
                   // message.setText("RED:  You must continue jumping.");
                else;
                    //message.setText("BLACK:  You must continue jumping.");
                selectedRow = move.toRow;  // Так как только одна часть может быть перемещена, выберите ее.
                selectedCol = move.toCol;

                CreateBoard();
                return;
            }
        }

         /* Ход текущего игрока закончился, поэтому переходите к другому игроку.
          Получите законные ходы этого игрока.  Если у игрока нет законных ходов,
            то игра заканчивается. */

        if (currentPlayer == CheckersData.WHITE) {
            currentPlayer = CheckersData.BLACK;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null);
               // gameOver("BLACK has no moves.  RED wins.");
            else if (legalMoves[0].isJump());
                //message.setText("BLACK:  Make your move.  You must jump.");
            else;
               // message.setText("BLACK:  Make your move.");
        }
        else {
            currentPlayer = CheckersData.WHITE;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null);
                //gameOver("RED has no moves.  BLACK wins.");
            else if (legalMoves[0].isJump());
               // message.setText("RED:  Make your move.  You must jump.");
            else;
               // message.setText("RED:  Make your move.");
        }

         /* Установите selectedRow = -1 для записи того, что игрок еще не выбрал
          фигуру, которую нужно двигать. */

        selectedRow = -1;

         /* Из вежливости к пользователю, если все законные ходы используют одну и ту же фигуру, то
            выберите эту фигуру автоматически, чтобы пользователю не пришлось нажимать на нее
            чтобы выбрать её. */

        if (legalMoves != null) {
            boolean sameStartSquare = true;
            for (int i = 1; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow != legalMoves[0].fromRow
                        || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                    sameStartSquare = false;
                    break;
                }
            if (sameStartSquare) {
                selectedRow = legalMoves[0].fromRow;
                selectedCol = legalMoves[0].fromCol;
            }
        }

        /* Убедитесь, что доска перерисована в новом состоянии. */


        CreateBoard();

    }  // конец doMakeMove();


    /**
     * Нарисуйте шахматный узор серым и светло-серым цветом.  Нарисуйте
     * шашки.  Если игра продолжается, остановите законные ходы.
     */


    public void CreateBoard(){

        ImageView Board = new ImageView(BoardImage);
        Board.setFitHeight(697);
        Board.setFitWidth(703);
        Main.root.getChildren().add(Board);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                switch (board.pieceAt(row,col)) {
                    case CheckersData.WHITE:
                        checkers[row][col] = new ImageView(WhiteChecker);
                        checkers[row][col].setFitWidth(78);
                        checkers[row][col].setFitHeight(78);
                        checkers[row][col].setX(38+col*78);
                        checkers[row][col].setY(36+row*78);
                        Main.root.getChildren().add(checkers[row][col]);
                        break;
                    case CheckersData.BLACK:
                        checkers[row][col] = new ImageView(BlackChecker);
                        checkers[row][col].setFitWidth(78);
                        checkers[row][col].setFitHeight(78);
                        checkers[row][col].setX(38+col*78);
                        checkers[row][col].setY(36+row*78);
                        Main.root.getChildren().add(checkers[row][col]);
                        break;
                    case CheckersData.WHITE_QUEEN:
                        checkers[row][col] = new ImageView(WhiteQueen);
                        checkers[row][col].setFitWidth(78);
                        checkers[row][col].setFitHeight(78);
                        checkers[row][col].setX(38+col*78);
                        checkers[row][col].setY(36+row*78);
                        Main.root.getChildren().add(checkers[row][col]);
                        break;
                    case CheckersData.BLACK_QUEEN:
                        checkers[row][col] = new ImageView(BlackQueen);
                        checkers[row][col].setFitWidth(78);
                        checkers[row][col].setFitHeight(78);
                        checkers[row][col].setX(38+col*78);
                        checkers[row][col].setY(36+row*78);
                        Main.root.getChildren().add(checkers[row][col]);
                        break;
                    default:
                        break;
                }
            }
        }

        if (gameInProgress) {
            // Сначала нарисуйте 2-пиксельную голубую границу вокруг фигур, которые можно перемещать.
            ImageView[] Squares = new ImageView[legalMoves.length];
            for (int i = 0; i < legalMoves.length; i++) {
                Squares[i] = new ImageView(Square);
                Squares[i].setFitWidth(78);
                Squares[i].setFitHeight(78);
                Squares[i].setX(38+legalMoves[i].fromCol*78);
                Squares[i].setY(36+legalMoves[i].fromRow*78);
                Main.root.getChildren().add(Squares[i]);
            }}
               /* Если фигура выбрана для перемещения (т. Е. если selectedRow >= 0), то
                нарисуйте 2-пиксельную белую рамку вокруг этой фигуры и нарисуйте зеленые границы
                вокруг каждого квадрата, на который можно переместить эту фигуру. */
        ImageView[] Stars = new ImageView[legalMoves.length];
            if (selectedRow >= 0) {
                for (int i = 0; i < legalMoves.length; i++) {
                    if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                        Stars[i] = new ImageView(Star);
                        Stars[i].setFitWidth(78);
                        Stars[i].setFitHeight(78);
                        Stars[i].setX(38+legalMoves[i].toCol*78);
                        Stars[i].setY(36+legalMoves[i].toRow*78);
                        Main.root.getChildren().add(Stars[i]);

                    }
                }
            }
        }



} // end class Board


package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;


public class Main extends Application {

    static Button NewGame = new Button(); // Создание кнопки "Новая игра"
    static Button GiveUp = new Button();  //  Создание кнопки "Сдаться"
    public static Group root = new Group();  //
    static public Label text = new Label();  //  Создания окна с сообщениями


    @Override
    public void start(Stage primaryStage) throws Exception {

        text.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.ITALIC, 17));
        text.setLayoutX(250);
        text.setLayoutY(700);
        text.setWrapText(true); // переносит текст сообщения на новую строку при необходимости
        text.setMaxWidth(200);  // настройка координат и размеров окна с сообщениями
        primaryStage.setTitle("Шашки"); // установка названия окна
        primaryStage.getIcons().add(new Image(new FileInputStream("src/sample/checkers.png"))); // установка иконки окна
        NewGame.setText("Новая игра");
        NewGame.setPrefSize(125,53);
        NewGame.setLayoutX(116);
        NewGame.setLayoutY(697);  // настройка параметров и координат кнопки новая игра
        GiveUp.setText("Сдаться");
        GiveUp.setPrefSize(125,53);
        GiveUp.setLayoutX(450);
        GiveUp.setLayoutY(697);  // настройка параметров и координат кнопки сдаться
        Board Game = new Board(); // создание доски с шашками (массив) и запуск игры

        // создание обработчика нажатий на клетки доски
        root.addEventHandler(MouseEvent.MOUSE_PRESSED, evt -> {
            if (!Game.gameInProgress)
                text.setText("Нажмите \"Новая игра\", чтобы начать игру.");
            else {
                int col = (int)(evt.getX() - 38) / 78;
                int row = (int)(evt.getY() - 36) / 78;
                if (col >= 0 && col < 8 && row >= 0 && row < 8)
                    Game.doClickSquare(row,col);
            }
        });
        NewGame.setOnAction(actionEvent -> Game.doNewGame());  // создание обработчика нажатий на кнопку и вызов соответствующей функции

        GiveUp.setOnAction(actionEvent -> Game.GiveUp()); // создание обработчика нажатий на кнопку и вызов соответствующей функции


        root.getChildren().add(text);
        root.getChildren().add(NewGame);
        root.getChildren().add(GiveUp); // размещение кнопок и окна сообщений на экране

        Scene scene = new Scene(root,703,773);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}




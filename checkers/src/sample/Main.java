package sample;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {



    static Button NewGame = new Button();
    static Button Resign = new Button();
    public static Group root = new Group();


    public Main() throws FileNotFoundException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Шашки");

        NewGame.setText("New Game");
        NewGame.setPrefSize(125,53);
        NewGame.setLayoutX(116);
        NewGame.setLayoutY(697);
        Resign.setText("Resign");
        Resign.setPrefSize(125,53);
        Resign.setLayoutX(450);
        Resign.setLayoutY(697);

        sample.Board Game = new Board();
        Game.doNewGame();
        root.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                if (Game.gameInProgress == false);
                    // message.setText("Click \"New Game\" to start a new game.");
                else {
                    int col = (int)(evt.getX() - 38) / 78;
                    int row = (int)(evt.getY() - 36) / 78;
                    if (col >= 0 && col < 8 && row >= 0 && row < 8)
                        Game.doClickSquare(row,col);
                }
            }
        });
        NewGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Game.doNewGame();
            }
        });

        Resign.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Game.doResign();
            }
        });
        root.getChildren().add(NewGame);
        root.getChildren().add(Resign);
        Scene scene = new Scene(root,703,773);

        primaryStage.setScene(scene);


        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}




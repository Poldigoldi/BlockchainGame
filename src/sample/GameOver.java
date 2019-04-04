package sample;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class GameOver {
    private GridPane pane = new GridPane();
    private boolean startAgain = false;

    GameOver() {
        create();
        adjust();
    }

    public boolean isStartAgain() {
        return startAgain;
    }

    public void setStartAgain() {
        this.startAgain = false;
    }

    private void create() {
        Label gameOver = new Label("GAME OVER!");
        Button tryAgain = new Button("TRY AGAIN?");
        gameOver.setTranslateX(550);
        gameOver.setTranslateY(150);
        tryAgain.setTranslateX(550);
        tryAgain.setTranslateY(160);
        pane.add(gameOver, 1, 0);
        pane.add(tryAgain, 1,1);
        pane.setMinWidth(300);
        pane.setMinHeight(500);

        tryAgain.setOnAction(this::tryAgainPress);
    }

    private void adjust() {
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);
    }

    public GridPane returnRoot() {
        return pane;
    }

    public void  tryAgainPress(ActionEvent e) {
        System.out.println("click");
        startAgain = true;
    }
}

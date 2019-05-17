package sample;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class HelpPopUp {
    private TextField popUp;
    private double posX, posY;


    HelpPopUp(String fullString, double posX, double posY) throws FileNotFoundException {
        this.posX = posX;
        this.posY = posY;
        popUp = new TextField(fullString);
        popUp.setTranslateX(posX);
        popUp.setTranslateY(posY);
        popUp.setDisable(true);
        popUp.setMinSize(300,50);
        popUp.setFont(Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 16));
        popUp.setStyle("-fx-control-inner-background: #000000; -fx-text-inner-color: #39ff14; -fx-opacity: 0.8;");
    }

    TextField getPopUp() {
        return popUp;
    }

    void setVisible(boolean visible) {
        popUp.visibleProperty().setValue(visible);
    }

    void setPosX(double posX) {
        this.posX = posX;
        popUp.setTranslateX(posX);
    }

    void setPosY(double posY) {
        this.posY = posY;
        popUp.setTranslateY(posY);
    }

    void setRadius(int radius) {
        posX = posX + radius;
        posY = posY - radius;
        popUp.setTranslateX(posX);
        popUp.setTranslateY(posY);
    }

    void setPopUpText(String text) {
        this.popUp.setText(text);
    }

    /*Use this method if you want to change the content and position of the popup*/
    public void handlePopUp(String content, double posX, double posY, int radius, boolean visible) {
        this.setPopUpText(content);
        this.setPosX(posX);
        this.setPosY(posY);
        this.setRadius(radius);
        this.setVisible(visible);
    }

}

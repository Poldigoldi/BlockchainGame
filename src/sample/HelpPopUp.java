package sample;

import javafx.scene.control.TextField;
public class HelpPopUp {
    private String fullString, visibleString;
    private double posX, posY;
    private TextField popUp;

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    HelpPopUp(String fullString, double posX, double posY) {
        this.fullString = fullString;
        this.posX = posX;
        this.posY = posY;
        popUp = new TextField(fullString);
        popUp.setTranslateX(posX);
        popUp.setTranslateY(posY);
        popUp.setDisable(true);
        popUp.setStyle("-fx-opacity: 1;");
    }

    TextField getPopUp() {
        return popUp;
    }

    void setVisible(boolean visible) {
        popUp.visibleProperty().setValue(visible);
    }
}

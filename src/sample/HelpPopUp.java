package sample;

import javafx.scene.control.TextField;

class HelpPopUp {
    private TextField popUp;

    HelpPopUp(String fullString, double posX, double posY) {
        popUp = new TextField(fullString);
        popUp.setTranslateX(posX);
        popUp.setTranslateY(posY);
        popUp.setDisable(true);
        popUp.setStyle("-fx-opacity: 0.6;");
    }

    TextField getPopUp() {
        return popUp;
    }

    void setVisible(boolean visible) {
        popUp.visibleProperty().setValue(visible);
    }
}

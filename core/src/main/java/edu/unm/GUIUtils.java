package edu.unm;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GUIUtils {

    String mainFontPath = App.class.getResource("/thempo.ttf").toExternalForm();

    public GUIUtils() {}

    public void createBtn(Button btn, int width, int height, int fontSize) {
        btn.setPrefSize(width, height);
        btn.setStyle("-fx-background-color: rgb(248, 208, 216)");
        btn.setFont(Font.loadFont(mainFontPath, fontSize));
        btn.setTextFill(Color.BLACK);
    }

    public void createLabel(Label label, int width, int height, int fontSize) {
        label.setPrefSize(width, height);
        label.setStyle("-fx-background-color: rgb(248, 208, 216)");
        label.setFont(Font.loadFont(mainFontPath, fontSize));
        label.setTextFill(Color.BLACK);
        label.setAlignment(Pos.CENTER);
    }
}

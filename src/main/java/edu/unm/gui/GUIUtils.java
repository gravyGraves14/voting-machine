package edu.unm.gui;

import edu.unm.App;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GUIUtils {

    String mainFontPath = GUIUtils.class.getResource("thempo.ttf").toExternalForm();

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

    public void createTextField(TextField textField, int width, int height, int fontSize){
        textField.setPrefSize(width, height);
        textField.setStyle("-fx-background-color: rgb(248, 208, 216)");
        textField.setFont(Font.loadFont(mainFontPath, fontSize));
        textField.setAlignment(Pos.CENTER);
    }

    public void createPasswordField(PasswordField passwordFieldField, int width, int height, int fontSize){
        passwordFieldField.setPrefSize(width, height);
        passwordFieldField.setStyle("-fx-background-color: rgb(248, 208, 216)");
        passwordFieldField.setFont(Font.loadFont(mainFontPath, fontSize));
        passwordFieldField.setAlignment(Pos.CENTER);
    }
}

package edu.unm.gui;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Objects;

public class GUIUtils {

    String mainFontPath = Objects.requireNonNull(GUIUtils.class.getResource("thempo.ttf")).toExternalForm();

    public GUIUtils() {}

    public void createBtn(Button btn, int width, int height, int fontSize) {
        btn.setPrefSize(width, height);
        btn.setStyle("-fx-background-color: rgb(248, 208, 216);" + "-fx-border-color: black;");
        //btn.setFont(Font.loadFont(mainFontPath, fontSize));
        btn.setTextFill(Color.BLACK);
    }

    public void createLabel(Label label, int width, int height, int fontSize) {
        label.setPrefSize(width, height);
        label.setStyle("-fx-background-color: rgb(248, 208, 216)");
        //label.setFont(Font.loadFont(mainFontPath, fontSize));
        label.setTextFill(Color.BLACK);
        label.setAlignment(Pos.CENTER);
    }

    public void createTextField(TextField textField, int width, int height, int fontSize){
        textField.setPrefSize(width, height);
        textField.setStyle("-fx-background-color: rgb(248, 208, 216)");
        //textField.setFont(Font.loadFont(mainFontPath, fontSize));
        textField.setAlignment(Pos.CENTER);
    }

    public void createPasswordField(PasswordField passwordFieldField, int width, int height, int fontSize){
        passwordFieldField.setPrefSize(width, height);
        passwordFieldField.setStyle("-fx-background-color: rgb(248, 208, 216)");
        //passwordFieldField.setFont(Font.loadFont(mainFontPath, fontSize));
        passwordFieldField.setAlignment(Pos.CENTER);
    }

    public void createRadio(RadioButton radioButton, int fontSize, ToggleGroup toggleGroup) {
        //radioButton.setFont(Font.loadFont(mainFontPath, fontSize));
        radioButton.setToggleGroup(toggleGroup);
    }

    public void createCheckBox(CheckBox checkBox, int fontSize) {
        //checkBox.setFont(Font.loadFont(mainFontPath, fontSize));
        checkBox.setStyle("-fx-background-color: rgb(248, 208, 216)");
    }

    public void addBackBtn(GridPane oldRoot, GridPane newRoot, int row, int col, Scene scene, int direction){
        Button backBtn = new Button();
        createBtn(backBtn, 125, 50, 25);

        //direction == 0 is back (left arrow), else forward (right) arrow
        if (direction == 0) {
            backBtn.setShape(new Polygon(0, 4, 5, 0, 5, 2, 20, 2, 20, 6, 5, 6, 5, 8));
        } else {
            backBtn.setShape(new Polygon(0, 2, 15, 2, 15, 0, 20, 4, 15, 8, 15, 6, 0, 6));
        }

        backBtn.setOnAction(event -> scene.setRoot(newRoot));

        oldRoot.add(backBtn, col, row);
    }

    public GridPane createRoot(int rows, int cols) {
        GridPane root = new GridPane();
        root.setStyle("-fx-background-color: rgb(195, 247, 200)");

        for (int i = 0; i < rows; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0 / rows);
            rc.setValignment(VPos.CENTER);
            root.getRowConstraints().add(rc);
        }

        for (int i = 0; i < cols; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / cols);
            cc.setHalignment(HPos.CENTER);
            root.getColumnConstraints().add(cc);
        }

        root.setHgap(10);
        root.setVgap(10);

        return root;
    }

    public void createPopUp(String text){
        Stage popup = new Stage();
        VBox popupContent = new VBox(10);
        Scene popupScene = new Scene(popupContent, 525, 125);
        popupContent.setStyle("-fx-background-color: rgb(248, 208, 216)");
        popup.setScene(popupScene);

        Label popupLabel = new Label(text);
        createLabel(popupLabel, 400, 100, 25);
        popupLabel.setWrapText(true);

        popupContent.getChildren().addAll(popupLabel);
        popupContent.setAlignment(Pos.CENTER);
        popup.show();
    }
}

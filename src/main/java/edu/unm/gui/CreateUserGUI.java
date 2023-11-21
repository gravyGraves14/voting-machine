package edu.unm.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class CreateUserGUI {
    private final GridPane root;

    public CreateUserGUI() {
        root = new GridPane();
        root.setStyle("-fx-background-color: rgb(195, 247, 200)");
        GUIUtils guiUtils = new GUIUtils();

        int numRows = 5 ;
        int numCols = 3;

        for (int i = 0 ; i < numRows ; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0 / numRows);
            rc.setValignment(VPos.BOTTOM);
            root.getRowConstraints().add(rc);
        }

        for (int i = 0 ; i < numCols ; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / numCols);
            cc.setHalignment(HPos.CENTER);
            root.getColumnConstraints().add(cc);
        }

        root.setHgap(10);
        root.setVgap(10);

        // Labels
        Label firstName = new Label("First Name: ");
        Label lastName = new Label("Last Name: ");
        Label passWord = new Label("Password: ");
        guiUtils.createLabel(firstName,250,100,25);
        guiUtils.createLabel(lastName,250,100,25);
        root.add(firstName,1,1);
        root.add(lastName,1,2);
        root.add(passWord,1,3);


    }

    public GridPane getRoot() { return root; }
}

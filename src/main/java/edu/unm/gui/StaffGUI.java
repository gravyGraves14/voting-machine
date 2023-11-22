package edu.unm.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class StaffGUI {

    private final GridPane root;

    public StaffGUI(Scene scene) {
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

        //Buttons
        Button createStaff = new Button("Create Staff");
        Button openBallot = new Button("Open Ballot");
        Button closeBallot = new Button("Close Ballot");


        guiUtils.createBtn(createStaff, 250, 100, 25);
        guiUtils.createBtn(openBallot, 250, 100, 25);
        guiUtils.createBtn(closeBallot, 250, 100, 25);


        root.add(createStaff, 1, 1);
        root.add(openBallot, 1, 2);
        root.add(closeBallot, 1, 3);

        createStaff.setOnAction(event -> {
            CreateUserGUI createUserGUI = new CreateUserGUI(0);
            scene.setRoot(createUserGUI.getRoot());
        });

    }

    public GridPane getRoot() { return root; }
}

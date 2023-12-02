package edu.unm.gui;

import edu.unm.entity.PaperBallot;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;

public class StaffGUI {

    private final GridPane root;

    public StaffGUI(Scene scene) {
        GUIUtils guiUtils = new GUIUtils();
        root = guiUtils.createRoot(5, 3);

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
            guiUtils.addBackBtn(createUserGUI.getRoot(), root, 0 ,0, scene, 0);
            scene.setRoot(createUserGUI.getRoot());
        });

        openBallot.setOnAction(event -> {

            // 1. Authenticate user - they MUST be an admin-level user
            // 2. Before ballot is open, users cannot use the following buttons:
            //    -> Electronic Voting
            //    -> Tabulator
            // 3. While ballot is open, users can use ALL buttons
        });

        closeBallot.setOnAction(event -> {
            // 1. Authenticate user - they MUST be an admin-level user
            // 2. After ballot is closed, users cannot use the following buttons:
            //    -> Electronic Voting
            //    -> Tabulator
        });
    }

    public GridPane getRoot() { return root; }
}

package edu.unm.gui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

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
            // ...
            LoginStaffGUI loginStaffGUI = new LoginStaffGUI(scene, 2);
            guiUtils.addBackBtn(loginStaffGUI.getRoot(), root, 0 ,0, scene, 0);
            scene.setRoot(loginStaffGUI.getRoot());

        });

        closeBallot.setOnAction(event -> {
            // ...
            LoginStaffGUI loginStaffGUI = new LoginStaffGUI(scene, 3);
            guiUtils.addBackBtn(loginStaffGUI.getRoot(), root, 0 ,0, scene, 0);
            scene.setRoot(loginStaffGUI.getRoot());
        });
    }

    public GridPane getRoot() { return root; }
}

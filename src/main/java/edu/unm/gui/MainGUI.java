package edu.unm.gui;

import edu.unm.votingdevice.GevGUI;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class MainGUI {
    private final GridPane root;
    private final Stage primaryStage;
    private final Scene scene;

    public MainGUI(Scene scene, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.scene = scene;
        GUIUtils guiUtils = new GUIUtils();

        //Set up the Layout
        root = new GridPane();
        root.setStyle("-fx-background-color: rgb(195, 247, 200)");

        int numRows = 6 ;
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
        Button gevBtn = new Button("Electronic Voting");
        Button tabBtn = new Button("Tabulator");
        Button votBtn = new Button("Voter Registration");
        Button staffBtn = new Button("Staff");

        guiUtils.createBtn(gevBtn, 250, 100, 25);
        guiUtils.createBtn(tabBtn, 250, 100, 25);
        guiUtils.createBtn(votBtn, 250, 100, 25);
        guiUtils.createBtn(staffBtn, 250, 100, 25);

        root.add(gevBtn, 1, 1);
        root.add(tabBtn, 1, 2);
        root.add(votBtn, 1, 3);
        root.add(staffBtn, 1, 4);

        //Vote count
        Label totVot = new Label("Total Votes:\n100");
        guiUtils.createLabel(totVot, 150, 100, 20);
        root.add(totVot, 0, 0);

        scene.setRoot(root);

        //Button Actions
        gevBtn.setOnAction(event -> {
            GevGUI gevGUI = new GevGUI();
            scene.setRoot(gevGUI.getRoot());
        });

        staffBtn.setOnAction(event -> {
            StaffGUI staffGUI = new StaffGUI(scene);
            scene.setRoot(staffGUI.getRoot());
        });
    }

    public void runStage() {
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

package edu.unm.gui;

import edu.unm.entity.PaperBallot;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;

public class MainGUI {
    private final GridPane root;
    private final Stage primaryStage;
    private final Scene scene;

    public MainGUI(Scene scene, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.scene = scene;
        GUIUtils guiUtils = new GUIUtils();

        //Paper Ballot setup
        PaperBallot paperBallot = new PaperBallot();
        try {
            paperBallot.createPaperBallot();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Set up the Layout
        root = guiUtils.createRoot(6, 3);

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

        scene.setRoot(root);

        //Button Actions
        gevBtn.setOnAction(event -> {
            if(Configuration.isGevEnabled()){
                GevGUI gevGUI = new GevGUI(scene);
                guiUtils.addBackBtn(gevGUI.getRoot(), root, 0 ,0, scene, 0);
                scene.setRoot(gevGUI.getRoot());
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("FEATURE DISABLED");
                alert.setContentText("Voting is currently closed.");
                alert.showAndWait();
            }
        });

        tabBtn.setOnAction(event -> {
            if(Configuration.isTabEnabled()){
                TabulationGUI tabulationGUI = new TabulationGUI(scene);
                guiUtils.addBackBtn(tabulationGUI.getRoot(), root, 0, 0, scene, 0);
                scene.setRoot(tabulationGUI.getRoot());
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("FEATURE DISABLED");
                alert.setContentText("Voting is currently closed.");
                alert.showAndWait();
            }
        });

        staffBtn.setOnAction(event -> {
            StaffGUI staffGUI = new StaffGUI(scene);
            guiUtils.addBackBtn(staffGUI.getRoot(), root, 0 ,0, scene, 0);
            scene.setRoot(staffGUI.getRoot());
        });

        votBtn.setOnAction(event -> {
            if(Configuration.isVoterRegEnabled()){
                VoterReg voterReg = new VoterReg(scene);
                guiUtils.addBackBtn(voterReg.getRoot(), root, 0, 0, scene, 0);
                scene.setRoot(voterReg.getRoot());
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("FEATURE DISABLED");
                alert.setContentText("Voting has ended, registration is now closed.");
                alert.showAndWait();
            }
        });
    }

    public void runStage() {
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

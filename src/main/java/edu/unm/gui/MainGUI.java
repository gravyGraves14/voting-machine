package edu.unm.gui;

import edu.unm.entity.PaperBallot;
import javafx.scene.Scene;
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

        //Vote count
        Label totVot = new Label("Total Votes:\n100");
        guiUtils.createLabel(totVot, 150, 100, 20);
        root.add(totVot, 2, 0);

        scene.setRoot(root);

        //Button Actions
        gevBtn.setOnAction(event -> {

            try {
                System.out.println(paperBallot.checkBallot());
            } catch (IOException e) {
                e.printStackTrace();
            }

            GevGUI gevGUI = new GevGUI(scene);
            guiUtils.addBackBtn(gevGUI.getRoot(), root, 0 ,0, scene, 0);
            scene.setRoot(gevGUI.getRoot());
        });

        tabBtn.setOnAction(event -> {
            TabulationGUI tabulationGUI = new TabulationGUI(scene);
            guiUtils.addBackBtn(tabulationGUI.getRoot(), root, 0, 0, scene, 0);
            scene.setRoot(tabulationGUI.getRoot());
        });

        staffBtn.setOnAction(event -> {
            StaffGUI staffGUI = new StaffGUI(scene);
            guiUtils.addBackBtn(staffGUI.getRoot(), root, 0 ,0, scene, 0);
            scene.setRoot(staffGUI.getRoot());
        });

        votBtn.setOnAction(event -> {
            VoterReg voterReg = new VoterReg(scene);
            guiUtils.addBackBtn(voterReg.getRoot(), root, 0, 0, scene, 0);
            scene.setRoot(voterReg.getRoot());
        });
    }


    public void runStage() {
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

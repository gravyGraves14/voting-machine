package edu.unm.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.Objects;

public class GevGUI {

    private final GridPane root;
    private final GUIUtils guiUtils = new GUIUtils();
    private final Scene scene;
    private int evType = 0;
    private String voter;

    public GevGUI(Scene scene) {
        this.scene = scene;

        //Set up the layout
        root = guiUtils.createRoot(4, 3);

        //Create gui items
        Label idLabel = new Label("Voter ID:");
        TextField idField = new TextField();
        Button enterBtn = new Button("Enter");

        guiUtils.createLabel(idLabel, 250, 100, 25);
        guiUtils.createTextField(idField, 250, 100, 25);
        guiUtils.createBtn(enterBtn, 250, 100, 25);

        root.add(idLabel, 1, 1);
        root.add(idField, 1, 2);
        root.add(enterBtn, 2, 3);

        //Button actions
        enterBtn.setOnAction(event -> {
            /*needs backend:
            authentication
            set voter as voted
             */
//            if (!Objects.equals(idField.getText(), "111111")) {
//                guiUtils.createPopUp("Invalid Voter ID");
//            }
//            else {
                voter = idField.getText();
                idField.setText("");
                scene.setRoot(createChoiceRoot());
//            }
        });
    }

    // Creates Root with MD and EV choices
    private GridPane createChoiceRoot() {
        GridPane choiceRoot = guiUtils.createRoot(4, 3);

        //Create buttons
        Button mdBtn = new Button("Marking Device");
        Button evBtn = new Button("Electronic Voting");

        guiUtils.createBtn(mdBtn, 250, 100, 25);
        guiUtils.createBtn(evBtn, 250, 100, 25);

        choiceRoot.add(mdBtn, 1, 1);
        choiceRoot.add(evBtn, 1, 2);

        //Button actions
        mdBtn.setOnAction(event -> {
            /*
            Needs backend:
            set something internally to know if user chose md or ev
             */
            evType = 1;
            createQstnGUIs();
        });

        evBtn.setOnAction(event -> {
            evType = 2;
            createQstnGUIs();
        });

        guiUtils.addBackBtn(choiceRoot, root, 0, 0, scene, 0);

        return choiceRoot;
    }

    private void createQstnGUIs() {
        //For testing
        String[] questions = {"Vote for the queen of CS", "Vote for the worst CS professor", "Vote for best Tech Titans Team Member"};
        String[][] qstnChcs = {{"Ester", "Emely"}, {"Darko", "Stefanovic"}, {"Ester", "Emely", "Manjil", "Mike", "Jacob", "Raju"}};

        //Create question roots
        QuestionGUI[] questionGUIS = new QuestionGUI[questions.length];
        for (int i = 0; i < questions.length; i++) {
            questionGUIS[i] = new QuestionGUI(i + 1, questions[i], qstnChcs[i].length, qstnChcs[i]);
        }

        //Back and forward arrows
        for (int i = 0; i < questions.length; i++) {
            if (i == 0) {
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), createChoiceRoot(), 0, 0, scene, 0);
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), questionGUIS[i + 1].getRoot(), 0, 2, scene, 1);
            }
            else if (i == questions.length - 1) {
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), questionGUIS[i - 1].getRoot(), 0, 0, scene, 0);

                GridPane submitRoot = createSubmitRoot(questionGUIS);
                guiUtils.addBackBtn(submitRoot, questionGUIS[i].getRoot(), 0, 0, scene, 0)
                ;
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), submitRoot, 0, 2, scene, 1);
            }
            else {
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), questionGUIS[i - 1].getRoot(), 0, 0, scene, 0);
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), questionGUIS[i + 1].getRoot(), 0, 2, scene, 1);
            }
        }

        scene.setRoot(questionGUIS[0].getRoot());
    }

    //Creates root with submit button
    private GridPane createSubmitRoot(QuestionGUI[] questionGUIS) {
        GridPane submitRoot = guiUtils.createRoot(3, 3);

        //Create submit button
        Button submitBtn = new Button("Submit");
        guiUtils.createBtn(submitBtn, 250, 100, 25);
        submitRoot.add(submitBtn, 1, 1);

        //Button action
        submitBtn.setOnAction(event -> {
            //Popup
            if(evType == 1) {
                guiUtils.createPopUp("Ballot Printed Successfully");
            }
            else {
                guiUtils.createPopUp("Ballot Printed and Logged Successfully");
            }

            /*
            Needs backend
            save results
             */
            //Get results
            for (QuestionGUI questionGUI : questionGUIS) {
                System.out.println(questionGUI.getSelected() + "\n");
            }
            scene.setRoot(root);
        });

        guiUtils.addBackBtn(submitRoot, root, 0, 0, scene, 0);

        return submitRoot;
    }

    public GridPane getRoot() {
        return root;
    }
}
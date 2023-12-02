package edu.unm.gui;

import edu.unm.dao.DAOFactory;
import edu.unm.dao.DAOUtils;
import edu.unm.dao.ElectorDAO;
import edu.unm.entity.Elector;
import edu.unm.entity.Questions;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;

public class GevGUI {

    private final GridPane root;
    private final GUIUtils guiUtils = new GUIUtils();
    private final Scene scene;
    private int evType = 0;
    private String voterId;

    public GevGUI(Scene scene) {
        this.scene = scene;

        //Set up the layout
        root = guiUtils.createRoot(4, 3);

        //Create gui items
        Label idLabel = new Label("SSN:");
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
            voterId = idField.getText();
            idField.setText("");

            ElectorDAO electorDAO = DAOFactory.create(ElectorDAO.class);
            List<Elector> allElectorList = new ArrayList<>();
            try {
                allElectorList = electorDAO.listAllElectors();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Elector elector = getElector(voterId, allElectorList);

            if (elector == null){
                showPopup("Not Registered", "You have not registered to vote.");
                return;
            }

            if (!elector.isQualifiedToVote()){
                showPopup("Ineligible Voter", "You must be at least 18 years to vote.");
                return;
            }

            if (elector == null){
                showPopup("Not Registered", "You have not yet registered.");
            }
            scene.setRoot(createChoiceRoot());
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
        Questions questions = new Questions();

        //Create question roots
        QuestionGUI[] questionGUIS = new QuestionGUI[questions.getNumQuestions()];
        for (int i = 0; i < questions.getNumQuestions(); i++) {
            questionGUIS[i] = new QuestionGUI(i + 1, questions.getQuestion(i), questions.getNumChoices(i),
                    questions.getQuestionChoices(i));
        }

        //Back and forward arrows
        for (int i = 0; i < questions.getNumQuestions(); i++) {
            if (i == 0) {
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), createChoiceRoot(), 0, 0, scene, 0);
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), questionGUIS[i + 1].getRoot(), 0, 2, scene, 1);
            }
            else if (i == questions.getNumQuestions() - 1) {
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

    public static Elector getElector(String id, List<Elector> allElectors){
        for (Elector elector : allElectors) {
            if (elector.getId().equals(id)) return elector;
        }
        return null;
    }

    private void showPopup(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public GridPane getRoot() {
        return root;
    }
}

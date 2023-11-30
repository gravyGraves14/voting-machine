package edu.unm.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.BufferedReader;
import java.io.FileReader;

public class GevGUI {

    private final GridPane root;
    private final GUIUtils guiUtils = new GUIUtils();
    private final Scene scene;
    private int evType = 0;
    private String voter;
    private List<String> questions;
    private List<String[]> questionChoices;
    private final String myFilePath = "src/main/java/edu/unm/gui/questions.txt";



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

    /**
     * Class I'm adding to see if we can read
     * a text file of questions. Still not sure
     * if this is the best place to put this method.
     * - Jacob
     */
        public void readQuestionsAndChoicesFromFile(String filePath) {

            questions = new ArrayList<>();
            questionChoices = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                StringBuilder currentQuestion = new StringBuilder();
                List<String> currentChoices = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    // If the line starts with a number, it is the beginning of a new question
                    if (line.matches("\\d+\\..*")) {
                        if (currentQuestion.length() > 0) {
                            // Save the previous question and choices
                            questions.add(currentQuestion.toString());
                            questionChoices.add(currentChoices.toArray(new String[currentChoices.size()]));
                            // Reset for the new Question
                            currentQuestion = new StringBuilder();
                            currentChoices.clear();
                        }

                        // Add the new question
                        currentQuestion.append(line.substring(line.indexOf('.') + 1).trim());
                    } else if (line.startsWith("-")) {
                        // If the line starts with a dash, it is a choice
                        currentChoices.add(line.substring(1).trim());
                    }
                }
                // Add the last question and choices after reaching the end of the file
                if (currentQuestion.length() > 0) {
                    questions.add(currentQuestion.toString());
                    questionChoices.add(currentChoices.toArray(new String[0]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    /**
     * The class I added above communicates
     * with createQstnGUIs. I took a picture just
     * in case this decides to act stupid.
     * - Jacob
     */

    private void createQstnGUIs() {
        //For testing
        //String[] questions = {"Vote for the queen of CS", "Vote for the worst CS professor", "Vote for best Tech Titans Team Member"};
        //String[][] qstnChcs = {{"Ester", "Emely"}, {"Darko", "Stefanovic"}, {"Ester", "Emely", "Manjil", "Mike", "Jacob", "Raju"}};

        readQuestionsAndChoicesFromFile(myFilePath);

        //Create question roots
        QuestionGUI[] questionGUIS = new QuestionGUI[questions.size()];
        for (int i = 0; i < questions.size(); i++) {
            String question = questions.get(i);
            String[] choices = questionChoices.get(i);
            questionGUIS[i] = new QuestionGUI(i + 1, question, choices.length, choices);
        }

        //Back and forward arrows
        for (int i = 0; i < questions.size(); i++) {
            if (i == 0) {
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), createChoiceRoot(), 0, 0, scene, 0);
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), questionGUIS[i + 1].getRoot(), 0, 2, scene, 1);
            }
            else if (i == questions.size() - 1) {
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

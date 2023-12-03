package edu.unm.gui;

import edu.unm.entity.QuestionOption;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class QuestionGUI {
    private final GridPane root;
    private final int numChoices;
    private final RadioButton[] choice;
    private final RadioButton other = new RadioButton("Other:");
    private final TextField otherTxt = new TextField();

    public QuestionGUI(int qstnNum, String qstn, int numChoices, List<QuestionOption> choices) {
        this.numChoices = numChoices;

        //Set up root
        GUIUtils guiUtils = new GUIUtils();
        root = guiUtils.createRoot(4 + numChoices, 3);
        root.setPadding(new Insets(30, 0, 0, 0));

        //Create gui items
        Label qstnNumLbl = new Label("Question #" + qstnNum);
        Label qstnLabel = new Label(qstn);

        guiUtils.createLabel(qstnNumLbl, 250, 100, 25);
        guiUtils.createLabel(qstnLabel, 500, 100, 25);
        qstnLabel.setWrapText(true);

        root.add(qstnNumLbl, 1, 0);
        root.add(qstnLabel, 1, 1, 1, 2);

        VBox chcs = new VBox(30);
        chcs.setAlignment(Pos.BOTTOM_LEFT);

        ToggleGroup radios = new ToggleGroup();

        //Create choice radio buttons

        choice = new RadioButton[numChoices];
        for (int i = 0; i < numChoices; i++) {
            choice[i] = new RadioButton(choices.get(i).getOption());
            guiUtils.createRadio(choice[i], 25, radios);
            chcs.getChildren().add(choice[i]);
        }

        HBox otherBox = new HBox(60);


        guiUtils.createRadio(other, 25, radios);
        otherBox.getChildren().add(other);

        //Other choice
        guiUtils.createTextField(otherTxt, 300, 15, 25);
        otherTxt.visibleProperty().bind(other.selectedProperty());
        otherBox.getChildren().add(otherTxt);

        chcs.getChildren().add(otherBox);
        root.add(chcs, 1, 3, 1, numChoices);

        root.setHgap(10);
        root.setVgap(10);
    }

    //Returns the selected option
    public String getSelected() {
        for(int i = 0; i < numChoices; i++) {
            if (choice[i].isSelected()) {
                return choice[i].getText();
            }
        }
        if (other.isSelected()) {
            return otherTxt.getText();
        }
        return "none";
    }

    public GridPane getRoot() {
        return root;
    }
}

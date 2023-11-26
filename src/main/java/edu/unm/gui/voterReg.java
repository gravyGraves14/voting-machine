package edu.unm.gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * This class is just basic user input for when
 * someone wants to register as voter, still need
 * to talk to front end to see how this should communicate
 * with backend
 */
public class voterReg {

    private final GridPane root;
    private final GUIUtils guiUtils = new GUIUtils();
    public voterReg(Scene scene) {

        root = guiUtils.createRoot(6, 3);

        Label firstName = new Label("First Name: ");
        TextField firstNameField = new TextField();

        Label lastName = new Label("Last Name: ");
        TextField lastNameField = new TextField();

        Label DOB = new Label("Date of birth mm/dd/yy: ");
        TextField dobField = new TextField();

        Label social = new Label("Social Security #: ");
        TextField socialField = new TextField();


        guiUtils.createLabel(firstName,250,100,25);
        guiUtils.createLabel(lastName,250,100,25);
        guiUtils.createLabel(DOB, 250, 100,  25);
        guiUtils.createLabel(social, 250, 100, 25);
        guiUtils.createTextField(firstNameField, 250, 100, 25);
        guiUtils.createTextField(lastNameField, 250, 100, 25);
        guiUtils.createTextField(dobField,250,100,25);
        guiUtils.createTextField(socialField,250,100,25);

        root.add(firstName, 0, 1);
        root.add(firstNameField, 1, 1);
        root.add(lastName, 0, 2);
        root.add(lastNameField, 1, 2);
        root.add(DOB, 0, 3);
        root.add(dobField, 1, 3);
        root.add(social, 0, 4);
        root.add(socialField, 1, 4);

    }

    public GridPane getRoot() {return root;}
}

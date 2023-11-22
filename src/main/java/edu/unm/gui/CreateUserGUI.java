package edu.unm.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class CreateUserGUI {
    private final GridPane root;

    // TODO: 11/21/2023 add confirm password label & passwordfields to
    public CreateUserGUI(int userType) {
        root = new GridPane();
        root.setStyle("-fx-background-color: rgb(195, 247, 200)");
        GUIUtils guiUtils = new GUIUtils();

        int numRows = 5 ;
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

        root.setHgap(5);
        root.setVgap(10);

        // Labels & Text fields
        Label firstName = new Label("First Name: ");
        TextField firstNameField = new TextField();

        Label lastName = new Label("Last Name: ");
        TextField lastNameField = new TextField();

        // Only for staff
        Label passWord = new Label("Password: ");
        PasswordField passwordField = new PasswordField();


        guiUtils.createLabel(firstName,250,100,25);
        guiUtils.createLabel(lastName,250,100,25);
        guiUtils.createTextField(firstNameField,250,100,25);
        guiUtils.createTextField(lastNameField,250,100,25);


        // Buttons
        Button enterBtn = new Button("Submit");
        guiUtils.createBtn(enterBtn,250,100,25);

        root.add(firstName,0,1);
        root.add(firstNameField,1,1);
        root.add(lastName,0,2);
        root.add(lastNameField,1,2);
        root.add(enterBtn,3,5);

        // If user is a voter, additional information must be requested
        if(userType == 1){
            Label socialSecurity = new Label("Social Security: ");
            TextField socialTextField = new TextField();
            guiUtils.createLabel(socialSecurity,250,100,25);

        }
        else {

            guiUtils.createLabel(passWord,250,100,25);
            guiUtils.createPasswordField(passwordField,250,100,25);
            root.add(passWord,0,3);
            root.add(passwordField,1,3);
        }

        enterBtn.setOnAction(event -> {
            firstNameField.getText();
            lastNameField.getText();
            passwordField.getText();
        });

        // generate password authentication pop-ups for open/close ballot buttons


    }

    public GridPane getRoot() { return root; }
}

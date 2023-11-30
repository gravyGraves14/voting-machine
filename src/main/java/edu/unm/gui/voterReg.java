package edu.unm.gui;

import edu.unm.dao.DAOFactory;
import edu.unm.dao.ElectorDAO;
import edu.unm.entity.Elector;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


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
        ElectorDAO electorDAO = DAOFactory.create(ElectorDAO.class);
        root = guiUtils.createRoot(6, 3);

        Label firstName = new Label("First Name: ");
        TextField firstNameField = new TextField();

        Label lastName = new Label("Last Name: ");
        TextField lastNameField = new TextField();

        Label DOB = new Label("Date of birth mm/dd/yyyy: ");
        TextField dobField = new TextField();

        Label social = new Label("Social Security #: ");
        TextField socialField = new TextField();

        Button registerButton = new Button("Submit");

        guiUtils.createLabel(firstName,250,100,25);
        guiUtils.createLabel(lastName,250,100,25);
        guiUtils.createLabel(DOB, 250, 100,  25);
        guiUtils.createLabel(social, 250, 100, 25);
        guiUtils.createTextField(firstNameField, 250, 100, 25);
        guiUtils.createTextField(lastNameField, 250, 100, 25);
        guiUtils.createTextField(dobField,250,100,25);
        guiUtils.createTextField(socialField,250,100,25);

        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        registerButton.setOnAction(e -> {
            // Handle register button click event
            String first = firstNameField.getText();
            String last = lastNameField.getText();
            Date dob;
            try {
                dob = new Date(inputFormat.parse(dobField.getText()).getTime());
                String formattedDate = dateFormat.format(dob);
                dob = Date.valueOf(formattedDate);
            } catch (ParseException ex) {
                showPopup("Invalid Date of Birth", "Please enter a valid date in the format mm/dd/yyyy");
                return;
            }

            String id = socialField.getText();

            if (id.length() != 9) {
                showPopup("Invalid Social Security Number", "Please enter a 9-digit social security number");
                return;
            }

            // Now you can use these values as needed (e.g., pass them to the backend)
//            System.out.println("First Name: " + first);
//            System.out.println("Last Name: " + last);
//            System.out.println("DOB: " + dob);
//            System.out.println("Social: " + id);



            Elector elector = new Elector(first, last, id, dob);
            try {
                if (electorDAO.isAlreadyRegistered(elector)){
                    showPopup("Already Registered", "You have already registered.");
                    return;
                }
            } catch (SQLException ex) {
                return;
            }

            if (!elector.isQualifiedToRegister()){
                showPopup("Ineligible Registration", "You must be at least 17 years old to register.");
                return;
            }

            try {
                electorDAO.addElector(elector);
                showSuccessPopUp("Success", "You have successfully registered!");
            } catch (SQLException ex) {
                return;
            }

        });

        root.add(firstName, 0, 1);
        root.add(firstNameField, 1, 1);
        root.add(lastName, 0, 2);
        root.add(lastNameField, 1, 2);
        root.add(DOB, 0, 3);
        root.add(dobField, 1, 3);
        root.add(social, 0, 4);
        root.add(socialField, 1, 4);
        root.add(registerButton, 0, 5, 2, 1);
    }

    private void showPopup(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccessPopUp(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public GridPane getRoot() {return root;}
}

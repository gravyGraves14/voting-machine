/**
 * Author: Raju Nayak
 */

package edu.unm.gui;

import edu.unm.dao.DAOFactory;
import edu.unm.dao.StaffDAO;
import edu.unm.entity.Staff;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;

public class LoginStaffGUI {
    private final GridPane root;

    public LoginStaffGUI() {
        StaffDAO staffDAO = DAOFactory.create(StaffDAO.class);
        GUIUtils guiUtils = new GUIUtils();
        root = guiUtils.createRoot(5, 3);

        // Labels & Text fields for First Name, Last Name, and Password
        Label firstNameLabel = new Label("First Name: ");
        TextField firstNameField = new TextField();

        Label lastNameLabel = new Label("Last Name: ");
        TextField lastNameField = new TextField();

        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();

        guiUtils.createLabel(firstNameLabel, 250, 100, 25);
        guiUtils.createLabel(lastNameLabel, 250, 100, 25);
        guiUtils.createLabel(passwordLabel, 250, 100, 25);
        guiUtils.createTextField(firstNameField, 250, 100, 25);
        guiUtils.createTextField(lastNameField, 250, 100, 25);
        guiUtils.createPasswordField(passwordField, 250, 100);

        // Login Button
        Button loginBtn = new Button("Login");
        guiUtils.createBtn(loginBtn, 250, 100, 25);

        loginBtn.setOnAction(event -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String password = passwordField.getText();});

        // Add components to the root
        root.add(firstNameLabel, 0, 1);
        root.add(firstNameField, 1, 1);
        root.add(lastNameLabel, 0, 2);
        root.add(lastNameField, 1, 2);
        root.add(passwordLabel, 0, 3);
        root.add(passwordField, 1, 3);
        root.add(loginBtn, 2, 4);
    }

    private void showSuccessPopUp(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorPopUp(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public GridPane getRoot() { return root; }
}
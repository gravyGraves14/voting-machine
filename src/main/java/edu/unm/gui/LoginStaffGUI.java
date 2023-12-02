/**
 * Author: Raju Nayak
 */

package edu.unm.gui;

import com.sun.xml.bind.v2.TODO;
import edu.unm.dao.DAOFactory;
import edu.unm.dao.StaffDAO;
import edu.unm.entity.Staff;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;

public class LoginStaffGUI {
    private final GridPane root;

    public LoginStaffGUI() {
        GUIUtils guiUtils = new GUIUtils();
        root = guiUtils.createRoot(5, 3);

        // Labels & Text fields for First Name, Last Name, and Password
        Label staffIdlabel = new Label("Staff Id: ");
        TextField staffIdField = new TextField();

        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();

        guiUtils.createLabel(staffIdlabel, 250, 100, 25);
        guiUtils.createLabel(passwordLabel, 250, 100, 25);
        guiUtils.createTextField(staffIdField, 250, 100, 25);
        guiUtils.createPasswordField(passwordField, 250, 100);

        // Login Button
        Button loginBtn = new Button("Login");
        guiUtils.createBtn(loginBtn, 250, 100, 25);

        loginBtn.setOnAction(event -> {
            String staffID = staffIdField.getText();
            String password = passwordField.getText();

            if (staffID.isEmpty() || password.isEmpty()) {
                showErrorPopUp("","Either Staff ID or Password is missing!");
            }
            //TODO: call login authenticator
            if(staffID.equals("12345") && password.equals("123456")) {
                showSuccessPopUp("","Successful");
            }
            else {
                showErrorPopUp("", "Login Failed! Please try again!");
            }
        });

        // Add components to the root
        root.add(staffIdlabel, 0, 1);
        root.add(staffIdField, 1, 1);
        root.add(passwordLabel, 0, 2);
        root.add(passwordField, 1, 2);
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
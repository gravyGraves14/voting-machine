/**
 * Author: Raju Nayak
 */

package edu.unm.gui;

import edu.unm.service.UserService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.List;

public class LoginStaffGUI {
    private final GridPane root;
    private final Scene scene;

    // 1 is for Tabulation
    // 2 is for Opening/Closing Ballot
    private final int Mode;

    public LoginStaffGUI(Scene scene, int mode) {
        this.scene = scene;
        Mode = mode;
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

            try {
                if (staffID.isEmpty() || password.isEmpty()) {
                    showErrorPopUp("","Either Staff ID or Password is missing!");
                }
                else if((UserService.verifyStaff(staffID,password)) != null){
                    if(mode == 1){
                        ResultGUI resultGUI = new ResultGUI(this.scene);
                        guiUtils.addBackBtn(resultGUI.getRoot(), root, 0 ,0, scene, 0);
                        scene.setRoot(resultGUI.getRoot());
                    }else{
                        System.out.println("Getting worked on");
                    }

                }
                else showErrorPopUp("", "Login Failed! Please try again!");
            } catch (SQLException e) {
                return;
            }
        });

        // Add components to the root
        root.add(staffIdlabel, 0, 1);
        root.add(staffIdField, 1, 1);
        root.add(passwordLabel, 0, 2);
        root.add(passwordField, 1, 2);
        root.add(loginBtn, 2, 4);
    }

    private void showErrorPopUp(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public GridPane getRoot() { return root; }
}
/**
 * Authors: Raju Nayak, Emely Seheon, Ester Aguilera, Manjil Pradhan
 */

package edu.unm.gui;

import edu.unm.service.UserService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.sql.SQLException;

/**
 * The LoginStaffGUI class provides a graphical user interface for staff members to log in.
 * This class supports different modes of operation depending on the context in which
 * it is used, such as tabulating results, opening ballots, and closing ballots.
 * It includes input fields for staff ID and password, and implements authentication logic
 * to ensure only authorized staff can access specific functionalities.
 */
public class LoginStaffGUI {
    private final GridPane root;
    private final Scene scene;

    /**
     * Constructor for LoginStaffGUI.
     * Sets up the user interface including labels, text fields, and a login button.
     * The mode parameter determines the specific operation to be performed after login.
     * @param scene the primary scene for this GUI component
     * @param mode  determines the operation to be performed after successful login
     */
    public LoginStaffGUI(Scene scene, int mode) {
        // "mode" 1 is for Tabulation
        // "mode" 2 is for Opening Ballot
        // "mode" 3 is for Closing Ballot
        this.scene = scene;
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
                    showErrorPopUp("Either Staff ID or Password is missing!");
                }
                else if((UserService.verifyStaff(staffID,password) != null)){
                    if(mode == 1){
                        ResultGUI resultGUI = new ResultGUI(this.scene);
                        guiUtils.addBackBtn(resultGUI.getRoot(), root, 0 ,0, scene, 0);
                        scene.setRoot(resultGUI.getRoot());
                    }else if(mode == 2){
                        // OPENING BALLOT
                        if(UserService.verifyStaffAdmin(staffID, password)){
                            LoadSchemaGUI loadSchemaGUI = new LoadSchemaGUI(this.scene);
                            guiUtils.addBackBtn(loadSchemaGUI.getRoot(), root, 0, 0, scene, 0);
                            scene.setRoot(loadSchemaGUI.getRoot());
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Staff must be ADMIN to open ballot");
                            alert.showAndWait();
                        }
                    }else{
                        // CLOSING BALLOT
                        if(UserService.verifyStaffAdmin(staffID, password)){
                            Configuration.setGevEnabled(false);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("FEATURE DISABLED");
                            alert.setContentText("Ballot is now closed, voting has ended.");
                            alert.showAndWait();
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Staff must be ADMIN to close ballot");
                            alert.showAndWait();
                        }
                    }
                }
                else showErrorPopUp("Login Failed! Please try again.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        // Add components to the root
        root.add(staffIdlabel, 0, 1);
        root.add(staffIdField, 1, 1);
        root.add(passwordLabel, 0, 2);
        root.add(passwordField, 1, 2);
        root.add(loginBtn, 2, 4);
    }

    private void showErrorPopUp(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public GridPane getRoot() { return root; }
}
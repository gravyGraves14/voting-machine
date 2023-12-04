package edu.unm.gui;

import edu.unm.dao.DAOFactory;
import edu.unm.dao.StaffDAO;
import edu.unm.entity.Staff;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.apache.tinkerpop.gremlin.structure.T;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import java.util.random.RandomGeneratorFactory;

public class CreateUserGUI {
    private final GridPane root;

    // TODO: 11/21/2023 add confirm password label & passwordfields to
    public CreateUserGUI(int userType) {
        StaffDAO staffDAO = DAOFactory.create(StaffDAO.class);
        GUIUtils guiUtils = new GUIUtils();
        root = guiUtils.createRoot(5, 3);

        // Labels & Text fields
        Label firstName = new Label("First Name: ");
        TextField firstNameField = new TextField();

        Label lastName = new Label("Last Name: ");
        TextField lastNameField = new TextField();

        // Only for staff
        Label passWord = new Label("Password: ");
        PasswordField passwordField = new PasswordField();

        CheckBox adminCheckBox = new CheckBox("Admin?");

        guiUtils.createLabel(firstName,250,100,25);
        guiUtils.createLabel(lastName,250,100,25);
        guiUtils.createTextField(firstNameField,250,100,25);
        guiUtils.createTextField(lastNameField,250,100,25);
        guiUtils.createLabel(passWord,250,100,25);
        guiUtils.createPasswordField(passwordField,250,100);
        guiUtils.createCheckBox(adminCheckBox, 25);

        // Buttons
        Button enterBtn = new Button("Submit");
        guiUtils.createBtn(enterBtn,250,100,25);

        enterBtn.setOnAction(event -> {
            String first = firstNameField.getText();
            String last = lastNameField.getText();
            String password = passwordField.getText();
            String id = createId(first, last);      //generateRandomString(9);
            boolean isAdmin = adminCheckBox.isSelected();

            // check that staff does not already exist
            Staff staff = new Staff(id, first, last, isAdmin, password);
            Optional<Staff> staffList;
            try {
                staffList = staffDAO.getStaffById(staff.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(staffList.isPresent()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Staff already exists");
                alert.setContentText("You have already created an account");
                alert.showAndWait();
            }


            try {
                staffDAO.addStaff(staff);
                showSuccessPopUp("Success", "You have successfully registered!\n" +
                        "Your Staff ID is " + id + "\n" +
                        "Keep it safely, you will need it to log in");
            } catch (SQLException ex) {
                return;
            }
        });

        // generate password authentication pop-ups for open/close ballot buttons
        root.add(firstName,0,1);
        root.add(firstNameField,1,1);
        root.add(lastName,0,2);
        root.add(lastNameField,1,2);
        root.add(passWord,0,3);
        root.add(passwordField,1,3);
        root.add(adminCheckBox, 2, 1);
        root.add(enterBtn,2,4);


    }

    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private void showSuccessPopUp(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static String createId(String firstName, String lastName) {
        if (firstName.isEmpty() || lastName.isEmpty()) {
            return "Invalid ID";
        }
        return firstName.toLowerCase() + lastName.toLowerCase();
    }

    public GridPane getRoot() { return root; }
}

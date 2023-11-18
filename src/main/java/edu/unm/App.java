package edu.unm;

import edu.unm.dao.DAOFactory;
import edu.unm.dao.ElectorDAO;
import edu.unm.entity.Elector;
import edu.unm.gui.MainGUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Window Set-up
        primaryStage.setTitle("Ohio Voting System");
        primaryStage.setMaximized(true);
        MainGUI mainGUI = new MainGUI();
        Scene scene = new Scene(mainGUI.getRoot());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void testSQL() {
        try {
            // Assuming you have a valid Connection object
            ElectorDAO electorDAO = DAOFactory.create(ElectorDAO.class);

            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter social number:");
            String socialNumber = scanner.nextLine();

            System.out.println("Enter name:");
            String name = scanner.nextLine();

            System.out.println("Enter date of birth (YYYY-MM-DD):");
            String dobString = scanner.nextLine();
            Date dob = Date.valueOf(dobString);

            Elector newElector = new Elector(name, socialNumber, dob);

            // Add the elector
            if (electorDAO.addElector(newElector)) {
                System.out.println("Elector added successfully.");
            } else {
                System.out.println("Failed to add elector.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
        }
    }
}

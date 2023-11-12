package edu.unm;


import java.util.List;
import java.util.Scanner;
import java.sql.*;

public class ElectorApp {
    public static void main(String[] args) {
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

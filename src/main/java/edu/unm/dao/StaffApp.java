package edu.unm.dao;

import edu.unm.entity.Elector;
import edu.unm.entity.Staff;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class StaffApp {
    public static void main(String[] args) {
        try {
            // Assuming you have a valid Connection object
            ElectorDAO electorDAO = DAOFactory.create(ElectorDAO.class);

            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter social number:");
            String socialNumber = scanner.nextLine();

            System.out.println("Enter First Name:");
            String firstName = scanner.nextLine();

            System.out.println("Enter Last Name:");
            String lastName = scanner.nextLine();

            System.out.println("Enter date of birth (YYYY-MM-DD):");
            String dobString = scanner.nextLine();
            Date dob = Date.valueOf(dobString);

            Elector newElector = new Elector(firstName, lastName, socialNumber, dob);

            // Add the elector
            if (electorDAO.addElector(newElector)) {
                System.out.println("Elector added successfully.");
            } else {
                System.out.println("Failed to add elector.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
        }

        try {
            StaffDAO staffDAO = DAOFactory.create(StaffDAO.class);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter social number:");
            String id = scanner.nextLine();

            System.out.println("Enter First Name:");
            String firstName = scanner.nextLine();

            System.out.println("Enter Last Name:");
            String lastName = scanner.nextLine();

            Staff staff = new Staff(firstName, lastName, id);

            // Add the elector
            if (staffDAO.addStaff(staff)) {
                System.out.println("Staff added successfully.");
            } else {
                System.out.println("Failed to add staff.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
        }
    }
}

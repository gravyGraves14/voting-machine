package edu.unm.dao;

import edu.unm.entity.Elector;
import edu.unm.entity.Staff;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class UserApp {
    public static void main(String[] args) {
        //Test Code for Elector
        try {
            // Assuming you have a valid Connection object
            ElectorDAO electorDAO = DAOFactory.create(ElectorDAO.class);

            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter your ID:");
            String socialNumber = scanner.nextLine();

            System.out.println("Enter First Name:");
            String firstName = scanner.nextLine();

            System.out.println("Enter Last Name:");
            String lastName = scanner.nextLine();

            System.out.println("Enter date of birth (YYYY-MM-DD):");
            String dobString = scanner.nextLine();
            Date dob = Date.valueOf(dobString);

            Elector newElector = new Elector(firstName, lastName, socialNumber, dob);

            if (newElector.isQualifiedToVote()){
                System.out.println("Eligible to Vote");
            }else{
                System.out.println("Not Qualified to Vote");
            }
            // Add the elector
            if (newElector.isQualifiedToVote() && electorDAO.addElector(newElector)) {
                System.out.println("Elector added successfully.");
            } else {
                System.out.println("Failed to add elector.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
        }

        //Test code for Staff
        try {
            StaffDAO staffDAO = DAOFactory.create(StaffDAO.class);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter social number:");
            String id = scanner.nextLine();

            System.out.println("Enter First Name:");
            String firstName = scanner.nextLine();

            System.out.println("Enter Last Name:");
            String lastName = scanner.nextLine();

            System.out.println("Enter Password:");
            String password = scanner.nextLine();

            System.out.println("Are you admin(true or false only):");
            String admin = scanner.nextLine();

            boolean isAdmin = Boolean.parseBoolean(admin);

            Staff staff = new Staff(id, firstName, lastName, isAdmin, password);
            staff.setAdmin(true);
            staff.setPassword("abcdef");

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

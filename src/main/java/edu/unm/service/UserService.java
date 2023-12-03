package edu.unm.service;

import edu.unm.dao.DAOFactory;
import edu.unm.dao.ElectorDAO;
import edu.unm.dao.StaffDAO;
import edu.unm.entity.Elector;
import edu.unm.entity.Staff;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class UserService {

    public static Staff verifyStaff(String id, String password) throws SQLException {
        StaffDAO staffDAO = DAOFactory.create(StaffDAO.class);
        List<Staff> allStaff = staffDAO.listAllStaff();
        String hashed = hashPassword(password);
        for (Staff staff1 : allStaff){
            if (staff1.getId().equals(id) && staff1.getPassword().equals(hashed)){
                return staff1;
            }
        }
        return null;
    }

    public static boolean verifyStaffAdmin(String id, String password) throws SQLException {
        StaffDAO staffDAO = DAOFactory.create(StaffDAO.class);
        List<Staff> allStaff = staffDAO.listAllStaff();
        String hashed = hashPassword(password);
        for (Staff staff1 : allStaff){
            if (staff1.getId().equals(id)){
                if (staff1.isAdmin()) return true;
            }
        }
        return false;
    }

    public static void setVoted(Elector elector) throws SQLException {
        ElectorDAO electorDAO = DAOFactory.create(ElectorDAO.class);
        elector.setVoted();
        electorDAO.updateElector(elector);
    }


    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle exception (e.g., log it or throw a runtime exception)
            throw new RuntimeException("Error hashing password", e);
        }
    }
}

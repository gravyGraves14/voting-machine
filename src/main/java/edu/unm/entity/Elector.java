package edu.unm;

import java.sql.Date;
/**
 * Data Transfer Object containing user data from the {@code users} table.
 */
public class Elector {
    private final String name;
    private final String socialNumber;
    private final Date dob;

    public Elector(String name, String username, Date dob) {
        this.name = name;
        this.socialNumber = username;
        this.dob = dob;
    }



    public String getName(){return name;}

    public String getSocialNumber() {
        return socialNumber;
    }

    public Date getDob(){
        return dob;
    }

    @Override
    public String toString() {
        return "Elector{" +
                "Elector Social Number ='" + socialNumber + '\'' +
                ",Date of Birth='" + dob + '\'' +
                '}';
    }
}

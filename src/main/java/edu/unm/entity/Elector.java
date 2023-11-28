package edu.unm.entity;

import java.sql.Date;
/**
 * Data Transfer Object containing user data from the {@code users} table.
 */
public class Elector implements User{
    private final String firstName;
    private final String lastName;
    private final String id;
    private final Date dob;

    public Elector(String firstName, String lastName, String id, Date dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.dob = dob;
    }

    public String getFirstName(){return firstName;}

    public String getLastName(){return lastName;}

    public String getId() {
        return id;
    }

    public Date getDob(){
        return dob;
    }

    @Override
    public String toString() {
        return "Elector{" +
                "Elector ID ='" + id + '\'' +
                ",Date of Birth='" + dob + '\'' +
                '}';
    }
}

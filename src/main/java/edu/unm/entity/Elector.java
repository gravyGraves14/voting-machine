package edu.unm.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
/**
 * Data Transfer Object containing user data from the {@code users} table.
 */
public class Elector implements User{
    private final String firstName;
    private final String lastName;
    private final String id;
    private final Date dob;
    private int voted;

    public Elector(String firstName, String lastName, String id, Date dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.dob = dob;
        this.voted = 0;
    }

    public String getFirstName(){return firstName;}

    public String getLastName(){return lastName;}

    public String getId() {
        return id;
    }

    public Date getDob(){
        return dob;
    }
    public int getVoted(){return voted;}

    public void setVoted(){
        voted = 1;
    }

    public boolean isQualifiedToVote(){
        LocalDate today = LocalDate.now();
        LocalDate birthDate = dob.toLocalDate();
        Period age = Period.between(birthDate, today);
        return age.getYears() >= 18;
    }

    public boolean isQualifiedToRegister(){
        LocalDate today = LocalDate.now();
        LocalDate birthDate = dob.toLocalDate();
        Period age = Period.between(birthDate, today);
        return age.getYears() >= 17;
    }

    @Override
    public String toString() {
        return "Elector{" +
                "Elector ID ='" + id + '\'' +
                ",Date of Birth='" + dob + '\'' +
                '}';
    }
}

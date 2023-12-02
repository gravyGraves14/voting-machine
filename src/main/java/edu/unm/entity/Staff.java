package edu.unm.entity;

public class Staff implements User{
    String firstName;
    String lastName;
    boolean isAdmin;
    String password;
    String id;

    public Staff(String id, String firstName, String lastName, boolean isAdmin, String password){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
        this.password = password;
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){return lastName;}
    public void setAdmin(boolean bol){isAdmin = true;}
    public void setPassword(String password){this.password = password;}
    public boolean isAdmin() {return isAdmin;}
    public String getId(){return id;}
    public String getPassword(){return password;}


    @Override
    public String toString() {
        return "Staff{" +
                "Staff's Name ='" + firstName + " " + lastName + '\'' +
                "Staff ID = " + id + '\'' +
                ",Admin Staff='" + isAdmin + '\'' +
                '}';
    }
}

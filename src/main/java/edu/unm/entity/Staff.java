package edu.unm.entity;

/**
 * created by:
 * author: MichaelMillar
 */
public class Staff extends User {
    // TODO: Talk with Manjil about impelementation with elector and DB

    private Role role;
    private String password;

    protected Staff(String firstName, String lastName, Role role) {
        super(firstName, lastName);
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

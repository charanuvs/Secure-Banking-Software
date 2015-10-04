package edu.asu.securebanking.beans;

/**
 * Created by Vikranth on 10/4/2015.
 */
public class Employee extends AppUser {

    private String employeeID;

    private String firstName;

    private String lastName;

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

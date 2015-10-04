package edu.asu.securebanking.beans;

/**
 * Created by Vikranth on 10/4/2015.
 */
public class ExternalUser extends AppUser {

    /**
     * Customer ID
     */
    private String customerID;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
}

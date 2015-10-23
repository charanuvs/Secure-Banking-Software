package edu.asu.securebanking.beans;

/**
 * Created by Vikranth on 10/4/2015.
 */
public class Merchant extends ExternalUser {

    String merchantName;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}

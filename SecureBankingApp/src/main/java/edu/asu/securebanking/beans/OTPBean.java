package edu.asu.securebanking.beans;

import java.util.Date;

/**
 * Created by Vikranth on 10/29/2015.
 */
public class OTPBean {
    private Date createDate;

    private String userId;

    private String otp;

    public OTPBean(String otp) {
        this.createDate = new Date();
        this.otp = otp;
    }

    public OTPBean(String otp, String userId) {
        this.createDate = new Date();
        this.otp = otp;
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public boolean isValid() {
        return false;
    }
}

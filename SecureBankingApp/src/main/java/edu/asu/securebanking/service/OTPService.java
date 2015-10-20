package edu.asu.securebanking.service;

import edu.asu.securebanking.util.AppUtil;

/**
 * Created by Vikranth on 10/20/2015.
 */
public class OTPService {

    /**
     * @return otp
     */
    public String generateOTP() {
        return AppUtil.getRandomPwd(6);
    }

}

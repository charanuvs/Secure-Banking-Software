package edu.asu.securebanking.util;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.constants.AppConstants;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Vikranth on 10/18/2015.
 */
public final class AppUtil {

    private static DecimalFormat amtFormatter = new DecimalFormat("$0.00");

    /**
     * Private Constructor
     */
    private AppUtil() {
    }

    /**
     * Return url based on user role
     *
     * @param role
     * @return url
     */
    public static String getUrl(final String role) {
        return AppConstants.ROLE_URL_MAP.get(role);
    }

    /**
     * Get Random passwords of 8 characters
     *
     * @return pwd
     */
    public static String getRandomPwd() {
        return getRandomPwd(8);
    }

    /**
     * Get Random passwords of 'num' characters
     *
     * @return pwd
     */
    public static String getRandomPwd(int num) {
        return RandomStringUtils.randomAlphanumeric(num);
    }

    /**
     * @param date
     * @return dateAsString
     */
    public static String convertDateToString(Date date) {
        if (null == date)
            return "";

        return AppConstants.DATE_FORMAT.format(date);
    }

    /**
     * @param reqUser
     * @param dbUser
     */
    public static void copySubUserToDBUser(AppUser reqUser, AppUser dbUser) {
        if (reqUser == null || dbUser == null)
            return;
        // end
        dbUser.setName(reqUser.getName());
        dbUser.setEmail(reqUser.getEmail());
        dbUser.setAddress(reqUser.getAddress());
        dbUser.setPhoneNumber(reqUser.getPhoneNumber());
        dbUser.setStatus(reqUser.getStatus());
        dbUser.setGender(reqUser.getGender());
        dbUser.setDateString(reqUser.getDateString());
        dbUser.setSsn(reqUser.getSsn());
        // end
    }

    public static String getFormattedString(double amount) {
        return amtFormatter.format(amount);
    }

}

package edu.asu.securebanking.util;

import edu.asu.securebanking.constants.AppConstants;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

/**
 * Created by Vikranth on 10/18/2015.
 */
public final class AppUtil {

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

}

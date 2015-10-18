package edu.asu.securebanking.util;

import edu.asu.securebanking.constants.AppConstants;
import org.apache.commons.lang3.RandomStringUtils;

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

    public static String getRandomPwd() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}

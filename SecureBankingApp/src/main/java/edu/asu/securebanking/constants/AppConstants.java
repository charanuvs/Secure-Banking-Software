package edu.asu.securebanking.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vikranth
 */
public final class AppConstants {

    private AppConstants() {
    }

    /**
     * Default DOC title
     */
    public static final String DOC_TITLE = "XYZ Bank";

    public static final String ROLE_NORMAL = "ROLE_NORMAL";

    public static final String ROLE_MERCHANT = "ROLE_MERCHANT";

    public static final String ROLE_EMP = "ROLE_EMPLOYEE";

    public static final String ROLE_MANAGER = "ROLE_MANAGER";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static Map<String, String> GENDERS = new HashMap<String, String>();

    public static Map<String, String> ROLE_URL_MAP = new HashMap<String, String>();

    public static int MIN_AGE = 15;

    static {
        GENDERS.put("MALE", "Male");
        GENDERS.put("FEMALE", "Female");

        ROLE_URL_MAP.put(ROLE_NORMAL, "user");
        ROLE_URL_MAP.put(ROLE_EMP, "emp");
        ROLE_URL_MAP.put(ROLE_MANAGER, "manage");
        ROLE_URL_MAP.put(ROLE_ADMIN, "admin");
        ROLE_URL_MAP.put(ROLE_MERCHANT, "merch");
    }
}

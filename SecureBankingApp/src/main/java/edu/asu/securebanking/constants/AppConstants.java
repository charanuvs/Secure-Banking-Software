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

    public static final String GENDER_MALE = "MALE";

    public static final String GENDER_FEMALE = "FEMALE";

    public static final String GENDER_OTHER = "OTHER";

    public static final Map<String, String> GENDERS =
            new HashMap<String, String>();

    public static final Map<String, String> ROLE_URL_MAP =
            new HashMap<String, String>();

    public static final Map<String, String> EXTERNAL_USERS_ROLES =
            new HashMap<String, String>();

    public static final Map<String, String> INTERNAL_USERS_ROLES =
            new HashMap<String, String>();

    public static final int MIN_AGE = 15;

    public static final int MAX_EMAIL_LEN = 50;

    public static final String BANK_FROM_ADDR = "customerService@secureonlinebanking";

    public static String LOGGEDIN_USER = "loggedInUser";

    static {
        GENDERS.put("MALE", "Male");
        GENDERS.put("FEMALE", "Female");
        GENDERS.put("OTHER", "Other");

        ROLE_URL_MAP.put(ROLE_NORMAL, "user");
        ROLE_URL_MAP.put(ROLE_EMP, "emp");
        ROLE_URL_MAP.put(ROLE_MANAGER, "manage");
        ROLE_URL_MAP.put(ROLE_ADMIN, "admin");
        ROLE_URL_MAP.put(ROLE_MERCHANT, "merch");

        // External users role map
        EXTERNAL_USERS_ROLES.put(ROLE_NORMAL, "Individual");
        EXTERNAL_USERS_ROLES.put(ROLE_MERCHANT, "Merchant/Org");

        // Internal users role map
        INTERNAL_USERS_ROLES.put(ROLE_EMP, "Employee");
        INTERNAL_USERS_ROLES.put(ROLE_MANAGER, "System Manager");
        INTERNAL_USERS_ROLES.put(ROLE_ADMIN, "System Administrator");
    }
}

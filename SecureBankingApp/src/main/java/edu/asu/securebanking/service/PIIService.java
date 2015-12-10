package edu.asu.securebanking.service;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PII;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.dao.PIIDAO;
import edu.asu.securebanking.exceptions.AppBusinessException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Vikranth on 10/25/2015.
 */
public class PIIService {

    @Autowired
    private PIIDAO piiDAO;

    @Autowired
    private UserService userService;

    @Transactional
    public List<PII> getPIIRequests() throws AppBusinessException {

        List<PII> piiRequests = piiDAO.getRequests();

        return piiRequests;
    }

    /**
     * Authorize request
     *
     * @param userId
     * @param toUserId
     * @throws AppBusinessException
     */
    @Transactional
    public void authorizeRequest(String userId, String toUserId)
            throws AppBusinessException {
        // get the user
        AppUser user = userService.getInternalUser(userId);
        AppUser toUser = userService.getInternalUser(toUserId);

        // toUser has to be admin
        if (toUser == null || user == null
                || !AppConstants.ROLE_ADMIN.equals(toUser.getUserType())) {
            throw new AppBusinessException("Invalid request");
        }

        PII pii = piiDAO.getPII(userId, toUserId);

        if (pii == null || !AppConstants.PII_REQUEST.equals(pii.getType())) {
            throw new AppBusinessException("Request does not exist");
        } else {
            pii.setType(AppConstants.PII_AUTHORIZE);
            piiDAO.savePII(pii);
        }
    }

    /**
     * Request for user details
     *
     * @param userId
     */
    @Transactional
    public void requestUserDetails(String userId) throws
            AppBusinessException {
        // get the user
        AppUser user = userService.getInternalUser(userId);

        if (user == null) {
            throw new AppBusinessException("Invalid request");
        }

        PII pii = new PII();
        pii.setUser(user);
        pii.setFromUser(user);
        pii.setType(AppConstants.PII_AUTHORIZE);

        piiDAO.savePII(pii);
    }

    /**
     * @param userId
     * @param fromUserId
     * @return isAuthorized
     */
    @Transactional
    public boolean isAuthorizedInternalUser(String userId, String fromUserId) {
        PII pii = piiDAO.getPII(userId, fromUserId);

        if (pii != null &&
                AppConstants.PII_AUTHORIZE.equals(pii.getType()))
            return true;

        pii = piiDAO.getPII(userId, userId);

        if (pii != null && AppConstants.PII_AUTHORIZE.equals(pii.getType()))
            return true;

        return false;
    }

    /**
     * Authorize request
     *
     * @param userId
     * @param toUserId
     * @throws AppBusinessException
     */
    @Transactional
    public void makeRequestForInternalUser(String userId, String toUserId)
            throws AppBusinessException {
        // get the user
        AppUser user = userService.getInternalUser(userId);
        AppUser toUser = userService.getInternalUser(toUserId);

        // toUser has to be admin
        if (toUser == null || user == null
                || !AppConstants.ROLE_ADMIN.equals(toUser.getUserType())) {
            throw new AppBusinessException("Invalid request");
        }

        PII pii = piiDAO.getPII(userId, toUserId);

        if (pii != null) {
            if (AppConstants.PII_AUTHORIZE.equals(pii.getType()))
                throw new AppBusinessException("You are already authorized to view the User information");
        } else {
            pii = new PII();
            pii.setUser(user);
            pii.setFromUser(toUser);
            pii.setType(AppConstants.PII_REQUEST);

            piiDAO.savePII(pii);
        }
    }
}

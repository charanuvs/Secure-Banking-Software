package edu.asu.securebanking.dao;

import edu.asu.securebanking.beans.PII;

import java.util.List;

/**
 * Created by Vikranth on 10/25/2015.
 */
public interface PIIDAO {

    /**
     * Get all requests from internal users
     *
     * @return pii
     */
    List<PII> getRequests();

    /**
     * @param userId
     * @param fromUserId
     * @return pii
     */
    PII getPII(String userId, String fromUserId);

    /**
     * @param pii
     */
    void savePII(PII pii);

}

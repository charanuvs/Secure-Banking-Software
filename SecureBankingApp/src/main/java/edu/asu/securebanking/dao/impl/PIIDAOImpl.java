package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.PII;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.PIIDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Vikranth on 10/25/2015.
 */
public class PIIDAOImpl extends AbstractDAO implements PIIDAO {

    @Override
    public List<PII> getRequests() {

        Criteria requestCriteria = getSession().createCriteria(PII.class);
        requestCriteria.add(Restrictions.eq("type", AppConstants.PII_REQUEST));

        return requestCriteria.list();
    }

    @Override
    public PII getPII(String userId, String fromUserId) {
        Criteria criteria = getSession().createCriteria(PII.class);
        criteria.add(Restrictions.eq("user.userId", userId));
        criteria.add(Restrictions.eq("fromUser.userId", fromUserId));

        return (PII) criteria.uniqueResult();

    }

    @Override
    public void savePII(PII pii) {
        getSession().saveOrUpdate(pii);
    }
}

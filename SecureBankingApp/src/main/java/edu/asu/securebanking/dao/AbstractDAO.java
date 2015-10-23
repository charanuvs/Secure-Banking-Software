package edu.asu.securebanking.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by Vikranth on 10/12/2015.
 */
public class AbstractDAO {

    @Autowired
    @Qualifier("sessionFactory")
    protected SessionFactory sessionFactory;

    /**
     * Get current session
     *
     * @return session
     */
    protected Session getSession() {
        Session session = this.sessionFactory.getCurrentSession();
        return session;
    }
}

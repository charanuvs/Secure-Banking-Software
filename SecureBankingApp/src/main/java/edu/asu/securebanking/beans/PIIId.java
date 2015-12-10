package edu.asu.securebanking.beans;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Vikranth on 10/25/2015.
 */
@Embeddable
public class PIIId implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, insertable = false, updatable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FROM_USER_ID", nullable = false, insertable = false, updatable = false)
    private AppUser fromUser;

    public PIIId() {
    }

    public PIIId(AppUser user, AppUser fromUser) {
        this.user = user;
        this.fromUser = fromUser;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public AppUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(AppUser fromUser) {
        this.fromUser = fromUser;
    }

    @Override
    public String toString() {
        return "PIIId{" +
                "user=" + user +
                ", fromUser=" + fromUser +
                '}';
    }
}

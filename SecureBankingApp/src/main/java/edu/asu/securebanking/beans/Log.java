package edu.asu.securebanking.beans;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Zahed on 10/21/2015.
 */
@Entity
@Table(name = "log")
public class Log {
	
	@Id
	@Column(name = "LOG_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer logId;
	
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private AppUser user;
    
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_NO")
    private Account accountNum;
    
    @Column(name = "L_DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @Column(name = "SEVERITY", columnDefinition = "enum('MNG', 'EMP')")
    private String severity;
    
    @Column(name = "DESCRIPTION")
    private String logDescription;
    
    public Integer getLogId() {
		return logId;
	}
	
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
    
    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
    
    public Account getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(Account accountNum) {
        this.accountNum = accountNum;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    
    public String getSeverity() {
    	return severity;
    }
    
    public void setSeverity(String severity) {
    	this.severity = severity;
    }

    public String getLogDescription() {
    	return logDescription;
    }
    
    public void setLogDescription(String logDescription) {
    	this.logDescription = logDescription;
    }
    
    @Override
    public String toString() {
        return "Log{" +
                "logId=" + logId +
                ", user=" + user +
                ", accountNum = " + accountNum +
                ", date=" + date +
                ", severity='" + severity + '\'' +
                ", logDescription='" + logDescription + '\'' +
                '}';
    }
}
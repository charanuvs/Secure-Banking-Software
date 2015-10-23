package edu.asu.securebanking.beans;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Vikranth on 10/21/2015.
 */
@Entity
@Table(name = "TRANSACTION")
public class Transaction {

    @Id
    @Column(name = "T_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "TO_ACCOUNT")
    private Account toAccount;

    @ManyToOne
    @JoinColumn(name = "FROM_ACCOUNT")
    private Account fromAccount;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "T_TYPE", columnDefinition = "enum('TRANSFER', 'PAYMENT')")
    private String transactionType;

    @Column(name = "T_STATUS", columnDefinition = "enum('PENDING', 'COMPLETE')")
    private String status;

    @Column(name = "T_DATE")
    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "AUTHORISED_EMP")
    private AppUser authEmployee;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AppUser getAuthEmployee() {
        return authEmployee;
    }

    public void setAuthEmployee(AppUser authEmployee) {
        this.authEmployee = authEmployee;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", toAccount=" + toAccount +
                ", fromAccount=" + fromAccount +
                ", amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", authEmployee=" + authEmployee +
                '}';
    }
}

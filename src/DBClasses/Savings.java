package DBClasses;

import java.sql.Date;

public class Savings {
    private int userID;
    private int amt;
    private int total;
    private Date date;
    private String transactionType;
    private int transactionID;
    private String acct = "savings";

    public Savings(int userID, int amt, int total, Date date, String transactionType, int transactionID, String acct) {
        this.userID = userID;
        this.amt = amt;
        this.total = total;
        this.date = date;
        this.transactionType = transactionType;
        this.transactionID = transactionID;
        this.acct = acct;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }
}
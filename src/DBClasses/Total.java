package DBClasses;

import java.sql.Date;

public class Total {
    int userID;
    int totalAmt;
    Date date;
    String transactionType;
    int transactionID;

    public Total(int userID, int totalAmt, Date date, String transactionType, int transactionID) {
        this.userID = userID;
        this.totalAmt = totalAmt;
        this.date = date;
        this.transactionType = transactionType;
        this.transactionID = transactionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(int totalAmt) {
        this.totalAmt = totalAmt;
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
}

package DBClasses;

import java.sql.Date;

public abstract class Transaction {
    int userID;
    int amt;
    int oldTotal;
    int newTotal;
    Date date;

    public Transaction(int userID, int amt, int oldTotal, Date date) {
        this.userID = userID;
        this.amt = amt;
        this.oldTotal = oldTotal;
        this.date = date;
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

    public int getOldTotal() {
        return oldTotal;
    }

    public void setOldTotal(int oldTotal) {
        this.oldTotal = oldTotal;
    }

    public int getNewTotal() {
        return newTotal;
    }

    public void setNewTotal(int newTotal) {
        this.newTotal = newTotal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

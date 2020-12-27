package DBClasses;

import java.sql.Date;

public abstract class Transaction {
    int amt;
    Date date;

    public Transaction(int amt, Date date) {
        this.amt = amt;
        this.date = date;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

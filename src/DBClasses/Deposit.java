package DBClasses;

import java.sql.Date;

public abstract class Deposit extends Transaction {
    public Deposit(int userID, int amt, int oldTotal, Date date) {
        super(userID, amt, oldTotal, date);
    }

    public void addMoney(int amt) {
        newTotal = oldTotal + amt;
    }
}

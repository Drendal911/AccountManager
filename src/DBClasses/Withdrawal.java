package DBClasses;

import java.sql.Date;

public abstract class Withdrawal extends Transaction {

    public Withdrawal(int userID, int amt, int oldTotal, Date date) {
        super(userID, amt, oldTotal, date);
    }

    public void subtractMoney() {
        newTotal = oldTotal - amt;
    }
}

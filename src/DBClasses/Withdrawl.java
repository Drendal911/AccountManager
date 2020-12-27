package DBClasses;

import java.sql.Date;

public abstract class Withdrawl extends Transaction {

    public Withdrawl(int amt, Date date) {
        super(amt, date);
    }

    public int subMoney(int amt) {
        return (totalAmt - amt);
    }
}

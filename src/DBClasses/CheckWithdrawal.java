package DBClasses;

import java.sql.Date;

public class CheckWithdrawal extends Withdrawal{
    int checkNum;
    String payee;

    public CheckWithdrawal(int userID, int amt, int oldTotal, Date date, int checkNum, String payee) {
        super(userID, amt, oldTotal, date);
        this.checkNum = checkNum;
        this.payee = payee;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }
}

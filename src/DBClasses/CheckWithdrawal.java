package DBClasses;

import java.sql.Date;

public class CheckWithdrawal extends Withdrawal{
    private int checkNum;
    private String payee;
    private String acct;

    public CheckWithdrawal(int userID, int amt, int oldTotal, Date date, int checkNum, String payee, String acct) {
        super(userID, amt, oldTotal, date);
        this.checkNum = checkNum;
        this.payee = payee;
        this.acct = acct;
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

    public String getAccount() {
        return acct;
    }

    public void setAccount(String acct) {
        this.acct = acct;
    }
}

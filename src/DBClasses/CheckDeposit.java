package DBClasses;

import java.sql.Date;

public class CheckDeposit extends Deposit {
    private int checkNum;
    private String payer;
    private String acct;

    public CheckDeposit(int userID, int amt, int oldTotal, Date date, int checkNum, String payer, String acct) {
        super(userID, amt, oldTotal, date);
        this.checkNum = checkNum;
        this.payer = payer;
        this.acct = acct;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }
}

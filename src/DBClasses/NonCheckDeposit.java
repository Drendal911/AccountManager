package DBClasses;

import java.sql.Date;

public class NonCheckDeposit extends Deposit {
    String depositType;
    String depositMemo;
    String acct;

    public NonCheckDeposit(int userID, int amt, int oldTotal, Date date, String depositType, String depositMemo, String acct) {
        super(userID, amt, oldTotal, date);
        this.depositType = depositType;
        this.depositMemo = depositMemo;
        this.acct = acct;
    }

    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    public String getDepositMemo() {
        return depositMemo;
    }

    public void setDepositMemo(String depositMemo) {
        this.depositMemo = depositMemo;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }
}

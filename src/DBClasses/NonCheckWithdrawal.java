package DBClasses;

import java.sql.Date;

public class NonCheckWithdrawal extends Withdrawal{
    String withdrawalType;
    String withdrawalReason;

    public NonCheckWithdrawal(int userID, int amt, int oldTotal, Date date, String withdrawalType, String withdrawalReason) {
        super(userID, amt, oldTotal, date);
        this.withdrawalType = withdrawalType;
        this.withdrawalReason = withdrawalReason;
    }

    public String getWithdrawalType() {
        return withdrawalType;
    }

    public void setWithdrawalType(String withdrawalType) {
        this.withdrawalType = withdrawalType;
    }

    public String getWithdrawalReason() {
        return withdrawalReason;
    }

    public void setWithdrawalReason(String withdrawalReason) {
        this.withdrawalReason = withdrawalReason;
    }
}

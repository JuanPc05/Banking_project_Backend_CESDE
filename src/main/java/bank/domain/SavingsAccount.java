package bank.domain;

import bank.domain.enums.AccountState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountNumber, BigDecimal balance, String accountType, double interestRate) {
        super(accountNumber, balance, accountType);
        this.interestRate = interestRate;
    }

    public SavingsAccount(String accountNumber, BigDecimal balance, LocalDate dateOpened, AccountState accountState, String accountType, List<Transaction> transactions, double interestRate) {
        super(accountNumber, balance, dateOpened, accountState, accountType, transactions);
        this.interestRate = interestRate;
    }

    public SavingsAccount() {
        super();
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", dateOpened=" + dateOpened +
                ", accountState=" + accountState +
                ", accountType='" + accountType + '\'' +
                ", interestRate=" + interestRate +
                "} " + super.toString();
    }
}

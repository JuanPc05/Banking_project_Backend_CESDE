package bank.domain;

import bank.domain.enums.AccountState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckingAccount extends Account {
    private double overdraftPercentage;
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, BigDecimal balance, String accountType, double overdraftPercentage, double overdraftLimit) {
        super(accountNumber, balance, accountType);
        this.overdraftPercentage = overdraftPercentage;
        this.overdraftLimit = overdraftLimit;
    }

    public CheckingAccount(String accountNumber, BigDecimal balance, LocalDate dateOpened, AccountState accountState, String accountType, List<Transaction> transactions, double overdraftPercentage, double overdraftLimit) {
        super(accountNumber, balance, dateOpened, accountState, accountType, transactions);
        this.overdraftPercentage = overdraftPercentage;
        this.overdraftLimit = overdraftLimit;
    }

    public CheckingAccount() {
        super();
    }

    public double getOverdraftPercentage() {
        return overdraftPercentage;
    }

    public void setOverdraftPercentage(double overdraftPercentage) {
        this.overdraftPercentage = overdraftPercentage;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "overdraftPercentage=" + overdraftPercentage +
                ", overdraftLimit=" + overdraftLimit +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", dateOpened=" + dateOpened +
                ", accountState=" + accountState +
                ", accountType='" + accountType + '\'' +
                ", transactions=" + transactions +
                '}';
    }
}




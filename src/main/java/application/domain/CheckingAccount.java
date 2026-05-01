package application.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckingAccount extends Account {
    private double overdraftPercentage;
    private double overdraftLimit;
    private List<Transaction> transactions = new ArrayList<>();



    public CheckingAccount(String accountNumber, double initialBalance,
                           double overdraftLimit, double overdraftPercentage) {
        super(accountNumber, initialBalance, LocalDate.now(), "ACTIVE", "CHECKING", new ArrayList<>());
        this.overdraftLimit = overdraftLimit;
        this.overdraftPercentage = overdraftPercentage;
    }

    public double getOverdraftPercentage() { return overdraftPercentage; }
    public void setOverdraftPercentage(double overdraftPercentage) { this.overdraftPercentage = overdraftPercentage; }

    public double getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = overdraftLimit; }

    public List<Transaction> getTransactions() { return transactions; }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "overdraftPercentage=" + overdraftPercentage +
                ", overdraftLimit=" + overdraftLimit +
                ", transactions=" + transactions +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", dateOpened=" + dateOpened +
                ", accountState=" + accountState +
                ", accountType='" + accountType + '\'' +
                ", transactions=" + transactions +
                '}';
    }
}




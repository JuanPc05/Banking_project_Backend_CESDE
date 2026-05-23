package bank.domain;

import bank.domain.enums.AccountState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditCard extends Account {
    private double quota;
    private double debt;
    private int numberOfInstallments;
    private double creditLimit;

    public CreditCard(String accountNumber, BigDecimal balance, String accountType, double quota, double debt, int numberOfInstallments, double creditLimit) {
        super(accountNumber, balance, accountType);
        this.quota = quota;
        this.debt = debt;
        this.numberOfInstallments = numberOfInstallments;
        this.creditLimit = creditLimit;
    }

    public CreditCard(String accountNumber, BigDecimal balance, LocalDate dateOpened, AccountState accountState, String accountType, List<Transaction> transactions, double quota, double debt, int numberOfInstallments, double creditLimit) {
        super(accountNumber, balance, dateOpened, accountState, accountType, transactions);
        this.quota = quota;
        this.debt = debt;
        this.numberOfInstallments = numberOfInstallments;
        this.creditLimit = creditLimit;
    }

    public CreditCard() {
        super();
    }

    public double getQuota() {
        return quota;
    }

    public void setQuota(double quota) {
        this.quota = quota;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    @Override
    public String toString() {

        return String.format("Tarjeta %s |" +
                        " Cupo: $%.2f |" +
                        " Límite: $%.2f |" +
                        " Deuda: $%.2f |" +
                        " Cuotas: %d | " +
                        "Estado: %s",
                accountNumber,
                quota, creditLimit,
                debt,
                numberOfInstallments,
                accountState);
    }
}

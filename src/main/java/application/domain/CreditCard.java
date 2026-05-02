package application.domain;

import java.time.LocalDate;
import java.util.ArrayList;

public class CreditCard extends Account {
    private double quota;
    private double debt;
    private int numberOfInstallments;
    private double creditLimit;

    public CreditCard(String accountNumber, double quota, double creditLimit) {
        super(accountNumber, 0.0, LocalDate.now(), "ACTIVE", "CREDIT_CARD", new ArrayList<>());
        this.quota = quota;
        this.debt = 0.0;
        this.numberOfInstallments = 0;
        this.creditLimit = creditLimit;
    }

    public double getQuota() { return quota; }
    public void setQuota(double quota) { this.quota = quota; }

    public double getDebt() { return debt; }
    public void setDebt(double debt) { this.debt = debt; }

    public int getNumberOfInstallments() { return numberOfInstallments; }
    public void setNumberOfInstallments(int numberOfInstallments) { this.numberOfInstallments = numberOfInstallments; }

    public double getCreditLimit() { return creditLimit; }
    public void setCreditLimit(double creditLimit) { this.creditLimit = creditLimit; }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
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

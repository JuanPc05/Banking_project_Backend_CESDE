package bank.domain;

import bank.domain.enums.AccountState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditCard extends Account {
    private BigDecimal quota;
    private BigDecimal debt;
    private int numberOfInstallments;
    private BigDecimal creditLimit;

    public CreditCard(String accountNumber, BigDecimal balance, String accountType, BigDecimal quota, BigDecimal debt, int numberOfInstallments, BigDecimal creditLimit,int clientId) {
        super(accountNumber, balance, accountType, clientId);
        this.quota = quota;
        this.debt = debt;
        this.numberOfInstallments = numberOfInstallments;
        this.creditLimit = creditLimit;
    }

    public CreditCard(String accountNumber, BigDecimal balance, LocalDate dateOpened, AccountState accountState, String accountType, List<Transaction> transactions, BigDecimal quota, BigDecimal debt, int numberOfInstallments, BigDecimal creditLimit,int clientId) {
        super(accountNumber, balance, dateOpened, accountState, accountType, transactions, clientId);
        this.quota = quota;
        this.debt = debt;
        this.numberOfInstallments = numberOfInstallments;
        this.creditLimit = creditLimit;
    }


    public CreditCard(String accountNumber, BigDecimal quota, BigDecimal creditLimit,int clientId ) {

        super(accountNumber, BigDecimal.ZERO, "Tarjeta de Crédito" , clientId);

        this.quota = quota;

        this.creditLimit = creditLimit;
        this.debt = BigDecimal.ZERO; // La deuda inicializa en 0
        this.numberOfInstallments = 0;
    }

    public CreditCard() {
        super();
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
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

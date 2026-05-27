package bank.domain;

import bank.domain.enums.AccountState;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected BigDecimal balance;
    protected LocalDate dateOpened;
    protected AccountState accountState;
    protected String accountType;
    protected  int clientId;
    protected List<Transaction> transactions;

    // CONSTRUCTOR 1: Para cuentas NUEVAS (Lógica de negocio)
    // No pide fecha ni estado ni transacciones, porque el sistema los define por defecto.
    public Account(String accountNumber, BigDecimal balance, String accountType , int clientId) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.dateOpened = LocalDate.now(); // Fecha actual garantizada
        this.accountState = AccountState.ACTIVE; // Estado inicial garantizado
        this.accountType = accountType;
        this.transactions = new ArrayList<>();
        this.clientId = clientId;
    }

    // CONSTRUCTOR 2: Para cuentas EXISTENTES (Base de Datos / Mapper)
    // Recibe absolutamente todos los parámetros y los respeta tal cual vienen.
    public Account(String accountNumber, BigDecimal balance, LocalDate dateOpened,
                   AccountState accountState, String accountType, List<Transaction> transactions , int clientId) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.dateOpened = dateOpened; // Respeta la fecha original
        this.accountState = accountState; // Respeta si estaba bloqueada o activa
        this.accountType = accountType;
        this.transactions = transactions;
        this.clientId = clientId;
    }

    public Account() {

    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDate getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    public AccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }



    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", dateOpened=" + dateOpened +
                ", accountState=" + accountState +
                ", accountType='" + accountType + '\'' +
                ", transactions=" + transactions +
                '}';
    }
}

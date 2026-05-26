package bank.domain;

import bank.domain.enums.AccountState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckingAccount extends Account {
    private double overdraftPercentage;
    private BigDecimal overdraftLimit;

    // Constructor 1: Completo
    public CheckingAccount(String accountNumber, BigDecimal balance, String accountType, int clientId, double overdraftPercentage, BigDecimal overdraftLimit) {
        super(accountNumber, balance, accountType, clientId);
        this.overdraftPercentage = overdraftPercentage;
        this.overdraftLimit = overdraftLimit;
    }

    // Constructor 2: Con transacciones (Asegúrate de incluir clientId aquí también)
    public CheckingAccount(String accountNumber, BigDecimal balance, LocalDate dateOpened, AccountState accountState, String accountType, List<Transaction> transactions, int clientId, double overdraftPercentage, BigDecimal overdraftLimit) {
        // Debes pasar el clientId al super constructor aquí
        super(accountNumber, balance, dateOpened, accountState, accountType, transactions, clientId);
        this.overdraftPercentage = overdraftPercentage;
        this.overdraftLimit = overdraftLimit;
    }

    // Constructor 3: El que te estaba fallando (Corregido)
    public CheckingAccount(String accountNumber, BigDecimal balance, double overdraftPercentage, BigDecimal overdraftLimit, int clientId) {
        // CORRECCIÓN: Pasamos el clientId (int) en el lugar correcto, NO el balance
        super(accountNumber, balance, "Cuenta Corriente", clientId);

        this.overdraftPercentage = overdraftPercentage;
        this.overdraftLimit = overdraftLimit;
    }

    // Getters y Setters
    public double getOverdraftPercentage() {
        return overdraftPercentage;
    }

    public void setOverdraftPercentage(double overdraftPercentage) {
        this.overdraftPercentage = overdraftPercentage;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(BigDecimal overdraftLimit) {
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




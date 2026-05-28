package bank.domain;

import bank.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    protected int id; // Autogenerado por la BD
    protected String accountNumber; // Llave foránea hacia Account
    protected LocalDateTime timestamp;
    protected TransactionType transactionType;
    protected BigDecimal amount;
    protected BigDecimal balanceAfterTransaction;
    protected String description;

    // CONSTRUCTOR 1: Para transacciones NUEVAS (Lógica de negocio)
    public Transaction(String accountNumber, TransactionType transactionType, BigDecimal amount, BigDecimal balanceAfterTransaction, String description) {
        this.accountNumber = accountNumber;
        this.timestamp = LocalDateTime.now(); // Marca temporal real
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.description = description;
    }

    // CONSTRUCTOR 2: Para transacciones EXISTENTES (Desde la Base de Datos)
    public Transaction(int id, String accountNumber, LocalDateTime timestamp, TransactionType transactionType, BigDecimal amount, BigDecimal balanceAfterTransaction, String description) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.timestamp = timestamp; // Respeta la fecha histórica
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.description = description;
    }

    // CONSTRUCTOR 3: Vacío para el RowMapper
    public Transaction() {
    }

    // --- GETTERS (Solo lectura) ---
    public int getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionType getTransactionType() { return transactionType; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfterTransaction() { return balanceAfterTransaction; }
    public String getDescription() { return description; }

    // --- SETTERS (Solo para uso del RowMapper) ---
    public void setId(int id) { this.id = id; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setBalanceAfterTransaction(BigDecimal balanceAfterTransaction) { this.balanceAfterTransaction = balanceAfterTransaction; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return String.format("[%s] %-12s | Monto: $%,.2f | Saldo: $%,.2f | %s",
                timestamp.format(formatter),
                transactionType,
                amount,
                balanceAfterTransaction,
                description);
    }
}
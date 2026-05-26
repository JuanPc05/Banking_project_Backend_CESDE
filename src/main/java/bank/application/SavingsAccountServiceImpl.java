package bank.application;

import bank.domain.SavingsAccount;
import bank.domain.Transaction;
import bank.domain.enums.AccountState;
import bank.domain.enums.TransactionType;
import bank.application.inputs.SavingsAccountService;
import bank.application.ports.SavingsAccountRepositoryPort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class SavingsAccountServiceImpl implements SavingsAccountService {
    private final SavingsAccountRepositoryPort repository;

    public SavingsAccountServiceImpl(SavingsAccountRepositoryPort repository) {
        this.repository = repository;
    }

    // En SavingsAccountServiceImpl

    @Override
    public SavingsAccount createAccount(String accountNumber, BigDecimal initialBalance, int clientId) {
        Optional<SavingsAccount> existing = repository.findById(accountNumber);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("La cuenta " + accountNumber + " ya existe.");
        }

        // Aquí ya tienes tus 8 parámetros (String, BigDecimal, LocalDate, Enum, String, List, double, int)
        SavingsAccount newAccount = new SavingsAccount(
                accountNumber,
                initialBalance,
                LocalDate.now(),
                AccountState.ACTIVE,
                "SAVINGS",
                new ArrayList<>(),
                0.02,
                clientId
        );

        repository.save(newAccount);
        System.out.println("✅ Cuenta de ahorros creada con éxito.");
        return newAccount;
    }


    @Override
    public void deposit(String accountNumber, double amount) {
        Optional<SavingsAccount> accountOpt = repository.findById(accountNumber);

        if (accountOpt.isEmpty()) {
            System.out.println("Error: La cuenta " + accountNumber + " no existe.");
            return;
        }

        SavingsAccount account = accountOpt.get();

        if (account.getAccountState() != AccountState.ACTIVE) {
            System.out.println("Error: La cuenta no está activa.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Error: El monto a depositar debe ser mayor a cero.");
            return;
        }

        BigDecimal amountBd = BigDecimal.valueOf(amount);
        BigDecimal newBalance = account.getBalance().add(amountBd);
        account.setBalance(newBalance);

        Transaction depositRecord = new Transaction(
                account.getTransactions().size() + 1,
                TransactionType.DEPOSIT,
                amountBd,

                newBalance,
                "Depósito en cuenta de ahorros desde el servicio"
        );
        account.getTransactions().add(depositRecord);

        repository.update(account);
        System.out.println("Depósito exitoso. Nuevo saldo: $" + newBalance);
    }

    @Override
    public void withdraw(String accountNumber, double amount) {
        Optional<SavingsAccount> accountOpt = repository.findById(accountNumber);

        if (accountOpt.isEmpty()) {
            System.out.println("Error: La cuenta " + accountNumber + " no existe.");
            return;
        }

        SavingsAccount account = accountOpt.get();

        if (account.getAccountState() != AccountState.ACTIVE) {
            System.out.println("Error: La cuenta no está activa.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Error: El monto a retirar debe ser mayor a cero.");
            return;
        }
        BigDecimal amountBd = BigDecimal.valueOf(amount);

        if (account.getBalance().compareTo(amountBd) < 0) {
            System.out.println("Error: Saldo insuficiente. Tu saldo es: $" + account.getBalance());
            return;
        }

        BigDecimal newBalance = account.getBalance().subtract(amountBd);
        account.setBalance(newBalance);

        Transaction withdrawalRecord = new Transaction(
                account.getTransactions().size() + 1,
                TransactionType.WITHDRAWAL,
                amountBd,
                newBalance,
                "Retiro en cuenta de ahorros desde el servicio"
        );
        account.getTransactions().add(withdrawalRecord);

        repository.update(account);
        System.out.println("Retiro exitoso. Nuevo saldo: $" + newBalance);
    }

    @Override
    public void applyInterest(String accountNumber) {
        Optional<SavingsAccount> accountOpt = repository.findById(accountNumber);

        if (accountOpt.isEmpty()) {
            System.out.println("Error: La cuenta " + accountNumber + " no existe.");
            return;
        }

        SavingsAccount account = accountOpt.get();

        if (account.getAccountState() != AccountState.ACTIVE) {
            System.out.println("Error: La cuenta no está activa para recibir intereses.");
            return;
        }

        BigDecimal interestRate = BigDecimal.valueOf(account.getInterestRate() / 100.0);
        BigDecimal interestAmount = account.getBalance().multiply(interestRate);
        BigDecimal newBalance = account.getBalance().add(interestAmount);

        Transaction interestRecord = new Transaction(
                account.getTransactions().size() + 1,
                TransactionType.DEPOSIT,
                interestAmount,
                newBalance,
                "Abono por liquidación de intereses"
        );
        account.getTransactions().add(interestRecord);

        repository.update(account);
        System.out.println("Intereses aplicados a la cuenta " + accountNumber + ": $" + interestAmount);
    }

    @Override
    public    SavingsAccount getAccount(String accountNumber) {
        Optional<SavingsAccount> accountOpt = repository.findById(accountNumber);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("La cuenta " + accountNumber + " no existe.");
        }
        return accountOpt.get();
    }

}

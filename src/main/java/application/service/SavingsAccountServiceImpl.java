package application.service;

import application.domain.SavingsAccount;
import application.domain.Transaction;
import application.domain.enums.AccountState;
import application.domain.enums.TransactionType;
import application.service.outputs.SavingsAccountService;
import application.service.ports.SavingsAccountRepositoryPort;

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
    public SavingsAccount createAccount(String accountNumber, double initialBalance) {
        Optional<SavingsAccount> existing = repository.findById(accountNumber);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("La cuenta " + accountNumber + " ya existe.");
        }

        SavingsAccount newAccount = new SavingsAccount(accountNumber, initialBalance, LocalDate.now(),"ACTIVE","SAVINGS",new ArrayList<>(),0.02);
        repository.save(newAccount);
        System.out.println("✅ Cuenta de ahorros creada con número: " + accountNumber);
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

        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);

        Transaction depositRecord = new Transaction(
                account.getTransactions().size() + 1,
                TransactionType.DEPOSIT,
                amount,
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

        if (account.getBalance() < amount) {
            System.out.println("Error: Saldo insuficiente. Tu saldo es: $" + account.getBalance());
            return;
        }

        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);

        Transaction withdrawalRecord = new Transaction(
                account.getTransactions().size() + 1,
                TransactionType.WITHDRAWAL,
                amount,
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

        double interestAmount = account.getBalance() * (account.getInterestRate() / 100);
        double newBalance = account.getBalance() + interestAmount;
        account.setBalance(newBalance);

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

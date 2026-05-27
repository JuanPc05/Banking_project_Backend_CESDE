package bank.application;

import bank.domain.SavingsAccount;
import bank.domain.Transaction;
import bank.domain.enums.AccountState;
import bank.domain.enums.TransactionType;
import bank.application.inputs.SavingsAccountService;
import bank.application.ports.ISavingsAccountRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class SavingsAccountServiceImpl implements SavingsAccountService {
    private final ISavingsAccountRepository repository;

    public SavingsAccountServiceImpl(ISavingsAccountRepository repository) {
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
    public void deposit(String accountNumber, BigDecimal amount) {
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

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Error: El monto a depositar debe ser mayor a cero.");
            return;
        }


        BigDecimal newBalance = account.getBalance().add(amount);
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
    public void withdraw(String accountNumber, BigDecimal amount) {
        // 1. Buscamos la cuenta
        Optional<SavingsAccount> accountOpt = repository.findById(accountNumber);

        // 2. Validación de existencia: ¡Esta es la clave!
        if (accountOpt.isEmpty()) {
            System.out.println("Error: La cuenta " + accountNumber + " no existe.");
            return; // Detiene la ejecución aquí mismo si no existe
        }

        SavingsAccount account = accountOpt.get();

        // 3. Validación de saldo suficiente
        if (account.getBalance().compareTo(amount) >= 0) {
            // Realizamos la operación en memoria
            account.setBalance(account.getBalance().subtract(amount));

            // 4. Persistimos el cambio en la base de datos
            repository.update(account);

            System.out.println("✅ Retiro realizado correctamente.");
        } else {
            System.out.println("Error: Saldo insuficiente para realizar el retiro.");
        }

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

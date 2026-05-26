package bank.application;

import bank.domain.CheckingAccount;
import bank.domain.Transaction;
import bank.domain.enums.TransactionType;
import bank.application.inputs.CheckingAccountService;
import bank.application.ports.CheckingAccountRepository;
import java.math.BigDecimal;
import java.util.List;

public class CheckingAccountServiceImpl implements CheckingAccountService {
    private final CheckingAccountRepository repository;

    public CheckingAccountServiceImpl(CheckingAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createAccount(CheckingAccount account) {
        repository.save(account);
    }

    @Override
    public CheckingAccount getAccount(String accountNumber) {
        return repository.findByAccountNumber(accountNumber);
    }

    @Override
    public List<CheckingAccount> getAllAccounts() {
        return repository.findAll();
    }

    @Override
    public void deposit(String accountNumber, double amount) {
        CheckingAccount account = repository.findByAccountNumber(accountNumber);
        if (account != null) {
            BigDecimal amountBd = BigDecimal.valueOf(amount);
            account.setBalance(account.getBalance().add(amountBd));

            // Registro de transacción
            account.getTransactions().add(new Transaction(
                    account.getTransactions().size() + 1,
                    TransactionType.DEPOSIT,
                    amountBd,
                    account.getBalance(),
                    "Depósito realizado"
            ));
            repository.update(account);
            System.out.println("\n✅ Depósito exitoso. Nuevo saldo: $" + account.getBalance());
        }
    }

    @Override
    public void withdraw(String accountNumber, double amount) {
        CheckingAccount account = repository.findByAccountNumber(accountNumber);
        BigDecimal amountBd = BigDecimal.valueOf(amount);

        // Lógica de Sobregiro: Saldo + Límite de sobregiro
        BigDecimal availableFunds = account.getBalance().add(account.getOverdraftLimit());

        if (account != null && availableFunds.compareTo(amountBd) >= 0) {
            account.setBalance(account.getBalance().subtract(amountBd));
            account.getTransactions().add(new Transaction(
                    account.getTransactions().size() + 1,
                    TransactionType.WITHDRAWAL,
                    amountBd,
                    account.getBalance(),
                    "Retiro realizado"
            ));
            repository.update(account);
            System.out.println("\n✅ Retiro exitoso. Nuevo saldo: $" + account.getBalance());
        } else {
            System.out.println("⚠️ Fondos insuficientes (incluso con sobregiro).");
        }
    }

    @Override
    public void transfer(String fromAccount, String toAccount, double amount) {
        CheckingAccount origin = repository.findByAccountNumber(fromAccount);
        CheckingAccount destination = repository.findByAccountNumber(toAccount);

        if (origin != null && destination != null) {
            BigDecimal amountBd = BigDecimal.valueOf(amount);
            BigDecimal availableFunds = origin.getBalance().add(origin.getOverdraftLimit());

            if (availableFunds.compareTo(amountBd) >= 0) {
                // 1. Descontar y sumar
                origin.setBalance(origin.getBalance().subtract(amountBd));
                destination.setBalance(destination.getBalance().add(amountBd));

                // 2. Registrar transacciones
                origin.getTransactions().add(new Transaction(
                        origin.getTransactions().size() + 1, TransactionType.TRANSFER_OUT,
                        amountBd, origin.getBalance(), "Transferencia a " + toAccount));

                destination.getTransactions().add(new Transaction(
                        destination.getTransactions().size() + 1, TransactionType.TRANSFER_IN,
                        amountBd, destination.getBalance(), "Transferencia de " + fromAccount));

                // 3. Persistir ambos cambios
                repository.update(origin);
                repository.update(destination);

                System.out.println("\n💸 Transferencia exitosa.");
            } else {
                System.out.println("⚠️ Fondos insuficientes para transferir.");
            }
        }
    }
}
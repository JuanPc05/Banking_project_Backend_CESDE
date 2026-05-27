package bank.infrastructure.out.adapter;

import bank.application.ports.ISavingsAccountRepository;
import bank.domain.SavingsAccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// 1. Asegúrate de añadir "implements ISavingsAccountRepository"
public class SavingsAccountRepository implements ISavingsAccountRepository {

    private final Map<String, SavingsAccount> database = new HashMap<>();

    @Override
    public void save(SavingsAccount account) {
        database.put(account.getAccountNumber(), account);
    }

    @Override
    public Optional<SavingsAccount> findById(String accountNumber) {
        return Optional.ofNullable(database.get(accountNumber));
    }

    @Override
    public List<SavingsAccount> findAll() {
        return List.copyOf(database.values());
    }

    @Override
    public void update(SavingsAccount account) {
        if (database.containsKey(account.getAccountNumber())) {
            database.put(account.getAccountNumber(), account);
        }
    }

    // 2. CORRECCIÓN: La interfaz ISavingsAccountRepository espera recibir un String,
    // no el objeto completo.
    @Override
    public void delete(String accountNumber) {
        database.remove(accountNumber);
    }
}
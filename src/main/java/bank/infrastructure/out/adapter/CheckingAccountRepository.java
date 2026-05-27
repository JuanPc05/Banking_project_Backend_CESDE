package bank.infrastructure.out.adapter;

import bank.application.ports.ICheckingAccountRepository;
import bank.domain.CheckingAccount;

import java.util.HashMap;
import java.util.Map;

public class CheckingAccountRepository implements ICheckingAccountRepository {
    private final Map<String, CheckingAccount> database = new HashMap<>();

    @Override
    public CheckingAccount findByAccountNumber(String accountNumber) {

        return database.get(accountNumber);
    }

    @Override
    public void update(CheckingAccount account) {

        database.put(account.getAccountNumber(), account);
    }

    @Override
    public void delete(String accountNumber) {

    }

    @Override
    public void save(CheckingAccount account) {
        database.put(account.getAccountNumber(), account);
    }

    @Override
    public java.util.List<CheckingAccount> findAll() {
        return new java.util.ArrayList<>(database.values());
    }
}

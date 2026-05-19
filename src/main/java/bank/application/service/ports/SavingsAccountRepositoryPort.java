package bank.application.service.ports;

import bank.domain.SavingsAccount;

import java.util.List;
import java.util.Optional;

public interface SavingsAccountRepositoryPort {

    void save(SavingsAccount savingsAccount);
    Optional<SavingsAccount> findById(String AccountNumber);
    List<SavingsAccount> findAll();
    void update(SavingsAccount savingsAccount);
    void delete(SavingsAccount savingsAccount);
}

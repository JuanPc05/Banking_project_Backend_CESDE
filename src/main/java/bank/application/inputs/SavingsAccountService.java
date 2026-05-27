package bank.application.inputs;

import bank.domain.SavingsAccount;

import java.math.BigDecimal;

public interface SavingsAccountService {
    void withdraw(String accountNumber, BigDecimal amount);
    void applyInterest(String accountNumber);
    void deposit(String accountNumber, BigDecimal amount);
    SavingsAccount getAccount(String accountNumber);

    // Esta es la firma que debe coincidir con tu clase:
    SavingsAccount createAccount(String accountNumber, BigDecimal initialBalance, int clientId);
}



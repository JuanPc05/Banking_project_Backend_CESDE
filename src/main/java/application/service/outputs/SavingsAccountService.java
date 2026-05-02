package application.service.outputs;

import application.domain.SavingsAccount;

public interface SavingsAccountService {
    void withdraw(String accountNumber, double amount);
    void applyInterest(String accountNumber);
    void deposit(String accountNumber, double amount);
    SavingsAccount getAccount(String accountNumber);
    SavingsAccount createAccount(String accountNumber, double initialBalance);

}

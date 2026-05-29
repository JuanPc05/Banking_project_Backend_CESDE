package bank.application.inputs;

import bank.domain.CheckingAccount;
import bank.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;


public interface CheckingAccountService {

    CheckingAccount getAccountByClientId(int clientId);

    void createAccount(CheckingAccount account);

    CheckingAccount getAccount(String accountNumber);

    List<CheckingAccount> getAllAccounts();

    void deposit(String accountNumber, BigDecimal amount);

    void withdraw(String accountNumber, BigDecimal amount);

    List<Transaction> getTransactionsByAccount(String accountNumber);

    void transfer(String fromAccount, String toAccount, BigDecimal amount);

    int getClientIdByAccountNumber(String accountNumber);



}

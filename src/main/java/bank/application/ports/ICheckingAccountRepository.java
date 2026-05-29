package bank.application.ports;

import bank.domain.CheckingAccount;
import java.util.List;


public interface ICheckingAccountRepository {


    void save(CheckingAccount account);
    CheckingAccount findByAccountNumber(String accountNumber);
    List<CheckingAccount> findAll();
    void update(CheckingAccount account);
    void delete(String accountNumber);
    CheckingAccount findByClientId(int clientId);

}









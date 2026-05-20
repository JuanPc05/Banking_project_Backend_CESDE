package bank.application.inputs;

import bank.domain.CreditCard;
import bank.domain.PurchaseResult;

import java.util.List;

public interface CreditCardService {
    PurchaseResult purchaseCreditCard(String cardNumber, double amount, int installments);
    void createCreditCard(CreditCard card);
    CreditCard getCard(String cardNumber);
    List<CreditCard> getAllCards();
    void pay(String cardNumber, double amount);
}






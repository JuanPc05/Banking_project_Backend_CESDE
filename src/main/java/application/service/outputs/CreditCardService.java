package application.service.outputs;

import application.domain.CreditCard;
import application.domain.PurchaseResult;
import application.domain.enums.TransactionType;

import java.util.List;

public interface CreditCardService {
    PurchaseResult purchaseCreditCard(String cardNumber, double amount, int installments);
    void createCreditCard(CreditCard card);
    CreditCard getCard(String cardNumber);
    List<CreditCard> getAllCards();
    void pay(String cardNumber, double amount);
}






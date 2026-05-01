package application.service.outputs;

import application.domain.CreditCard;
import application.domain.PurchaseResult;

import java.util.List;

public interface CreditCardService {
    void createCreditCard(CreditCard card);
    CreditCard getCard(String cardNumber);
    List<CreditCard> getAllCards();
    PurchaseResult purchaseCreditCard(String cardNumber , double amount , int installmets);
    void pay(String cardNumber, double amount );
}


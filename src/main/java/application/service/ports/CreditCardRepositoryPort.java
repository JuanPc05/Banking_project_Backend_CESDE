package application.service.ports;

import application.domain.CreditCard;
import java.util.List;

public interface CreditCardRepositoryPort {
    void saveCreditCard(CreditCard card);
    CreditCard findByCardNumber(String cardNumber);
    List<CreditCard> findAll();
    void updateCreditCard(CreditCard card);


}

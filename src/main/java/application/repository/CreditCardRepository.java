package application.repository;

import application.domain.CreditCard;
import application.service.ports.CreditCardRepositoryPort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreditCardRepository implements CreditCardRepositoryPort {


    List<CreditCard> cards = new ArrayList<>(
            Arrays.asList(
                    new CreditCard("ACC001", 1000.0, 5000.0),
                    new CreditCard("ACC002", 1500.0, 6000.0),
                    new CreditCard("ACC003", 2000.0, 7000.0),
                    new CreditCard("ACC004", 2500.0, 8000.0),
                    new CreditCard("ACC005", 3000.0, 9000.0)
            )
    );

    @Override
    public void saveCreditCard(CreditCard card) {
        cards.add(card);
    }

    @Override
    public CreditCard findByCardNumber(String cardNumber) {
        return cards.stream()
                .filter(c -> c.getAccountNumber().equals(cardNumber))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<CreditCard> findAll() {
        return new ArrayList<>(cards);
    }

    @Override
    public void updateCreditCard(CreditCard card) {
        CreditCard existing = findByCardNumber(card.getAccountNumber());
        if (existing != null) {
            cards.remove(existing);
            cards.add(card);
        }
    }
}

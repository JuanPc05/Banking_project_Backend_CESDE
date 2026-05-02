package application.service;

import application.domain.CreditCard;
import application.domain.Transaction;
import application.domain.enums.TransactionType;
import application.service.outputs.CreditCardService;
import application.domain.PurchaseResult;
import application.service.ports.CreditCardRepositoryPort;

import java.util.List;

public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepositoryPort creditCardRepositoryPort;

    public CreditCardServiceImpl(CreditCardRepositoryPort creditCardRepository) {
        this.creditCardRepositoryPort = creditCardRepository;
    }

    @Override
    public void createCreditCard(CreditCard card) {
        creditCardRepositoryPort.saveCreditCard(card);
    }

    @Override
    public CreditCard getCard(String cardNumber) {
        return creditCardRepositoryPort.findByCardNumber(cardNumber);
    }

    @Override
    public List<CreditCard> getAllCards() {
        return creditCardRepositoryPort.findAll();
    }

    @Override
    public PurchaseResult purchaseCreditCard(String cardNumber, double amount, int installments) {
        CreditCard card = creditCardRepositoryPort.findByCardNumber(cardNumber);
        if (card == null) {
            throw new IllegalArgumentException("Tarjeta no encontrada.");
        }

        if (card.getDebt() + amount > card.getQuota() || amount > card.getCreditLimit()) {
            throw new IllegalArgumentException("Cupo insuficiente o supera el límite de crédito.");
        }

        double rate = getRateByInstallments(installments);
        double cuota = calculateMonthlyInstallment(amount, rate, installments);
        double totalConInteres = cuota * installments;

        card.setDebt(card.getDebt() + amount);
        card.setNumberOfInstallments(installments);
        creditCardRepositoryPort.updateCreditCard(card);

        return new PurchaseResult(amount, installments, rate, cuota, totalConInteres, card.getDebt());
    }

    private double getRateByInstallments(int installments) {
        if (installments <= 1) {
            return 0.0;
        } else if (installments <= 3) {
            return 0.02;
        } else if (installments <= 6) {
            return 0.03;
        } else if (installments <= 12) {
            return 0.04;
        } else {
            return 0.05;
        }
    }

    private double calculateMonthlyInstallment(double amount, double monthlyRate, int installments) {
        if (monthlyRate == 0) {
            return amount / installments;
        }
        double numerator = amount * monthlyRate;
        double denominator = 1 - Math.pow(1 + monthlyRate, -installments);
        return numerator / denominator;
    }


    @Override
    public void pay(String cardNumber, double amount) {
        CreditCard card = creditCardRepositoryPort.findByCardNumber(cardNumber);
        if (card == null) {
            throw new IllegalArgumentException("Tarjeta no encontrada.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a pagar debe ser mayor que cero.");
        }

        if (amount > card.getDebt()) {
            throw new IllegalArgumentException("El monto excede la deuda actual.");
        }

        // Reducir la deuda
        card.setDebt(card.getDebt() - amount);

        // Si la deuda queda en cero, reiniciamos cuotas
        if (card.getDebt() == 0) {
            card.setNumberOfInstallments(0);
        }

        creditCardRepositoryPort.updateCreditCard(card);

        System.out.println("✅ Pago realizado correctamente. Deuda actual: $" + card.getDebt());
    }






}



package application.service;

import application.domain.CreditCard;
import application.service.outputs.CreditCardService;
import application.domain.PurchaseResult;
import application.service.ports.CreditCardRepositoryPort;

import java.util.List;

public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepositoryPort  creditCardRepositoryPort;

    public CreditCardServiceImpl(CreditCardRepositoryPort creditCardRepository) {
        this. creditCardRepositoryPort = creditCardRepository;
    }

    @Override
    public void createCreditCard(CreditCard card) {
        creditCardRepositoryPort.saveCreditCard(card);
    }

    @Override
    public CreditCard getCard(String cardNumber) {
        return  creditCardRepositoryPort.findByCardNumber(cardNumber);
    }

    @Override
    public List<CreditCard> getAllCards() {
        return  creditCardRepositoryPort.findAll();
    }

    @Override
    public PurchaseResult purchaseCreditCard(String cardNumber, double amount, int installments) {
        CreditCard card =  creditCardRepositoryPort.findByCardNumber(cardNumber);
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

    @Override
    public void pay(String cardNumber, double amount) {
        CreditCard card = creditCardRepositoryPort.findByCardNumber(cardNumber);
        if (card == null) {
            throw new IllegalArgumentException("Tarjeta no encontrada.");
        }

        if (amount > card.getDebt()) {
            throw new IllegalArgumentException("El pago excede la deuda actual.");
        }

        card.setDebt(card.getDebt() - amount);
        creditCardRepositoryPort.updateCreditCard(card);
    }

    // 🔹 Método auxiliar: tasa según número de cuotas
    private double getRateByInstallments(int installments) {
        if (installments <= 2) return 0.0;       // sin interés
        if (installments <= 6) return 0.019;     // 1.9% mensual
        return 0.023;                            // 2.3% mensual
    }

    // 🔹 Método auxiliar: cálculo de cuota mensual con fórmula de amortización
    private double calculateMonthlyInstallment(double capital, double rate, int n) {
        if (rate == 0) return capital / n; // sin interés
        double divisor = 1 - Math.pow(1 + rate, -n);
        return (capital * rate) / divisor;
    }
}


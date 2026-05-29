package bank.application;

import bank.domain.CreditCard;
import bank.application.inputs.CreditCardService;
import bank.domain.PurchaseResult;
import bank.application.ports.CreditCardRepositoryPort;

import java.math.BigDecimal;
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

    // ✅ MÉTODO PUENTE AÑADIDO: Permite a la vista buscar la tarjeta automáticamente usando el ID de sesión
    @Override
    public CreditCard getCardByClientId(int clientId) {
        return creditCardRepositoryPort.findByClientId(clientId);
    }

    @Override
    public List<CreditCard> getAllCards() {
        return creditCardRepositoryPort.findAll();
    }

    @Override
    public PurchaseResult purchaseCreditCard(String cardNumber, BigDecimal amount, int installments) {
        CreditCard card = creditCardRepositoryPort.findByCardNumber(cardNumber);
        if (card == null) {
            throw new IllegalArgumentException("Tarjeta no encontrada.");
        }

        // 1. Sumamos directamente de objeto a objeto: card.getDebt() + amount
        BigDecimal nuevaDeuda = card.getDebt().add(amount);

        // 2. Validamos los límites financieros usando .compareTo()
        if (nuevaDeuda.compareTo(card.getQuota()) > 0 || amount.compareTo(card.getCreditLimit()) > 0) {
            throw new IllegalArgumentException("Cupo insuficiente o supera el límite de crédito.");
        }

        // 3. Cálculos de tasas y cuotas mensuales
        double amountDouble = amount.doubleValue();
        double rate = getRateByInstallments(installments);
        double cuota = calculateMonthlyInstallment(amountDouble, rate, installments);
        double totalConInteres = cuota * installments;

        // 4. Guardamos el estado actualizado en el Dominio y Base de Datos
        card.setDebt(nuevaDeuda);
        card.setNumberOfInstallments(installments);
        creditCardRepositoryPort.updateCreditCard(card);

        // 5. Retornamos el DTO de resultado
        return new PurchaseResult(
                amountDouble,
                installments,
                rate,
                cuota,
                totalConInteres,
                nuevaDeuda.doubleValue()
        );
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
    public void pay(String cardNumber, BigDecimal amount) {
        CreditCard card = creditCardRepositoryPort.findByCardNumber(cardNumber);
        if (card == null) {
            throw new IllegalArgumentException("Tarjeta no encontrada.");
        }

        // 1. El monto a pagar debe ser mayor que cero
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a pagar debe ser mayor que cero.");
        }

        // 2. El monto no debe exceder la deuda actual
        if (amount.compareTo(card.getDebt()) > 0) {
            throw new IllegalArgumentException("El monto excede la deuda actual.");
        }

        // 3. Reducir la deuda restando con .subtract()
        BigDecimal nuevaDeuda = card.getDebt().subtract(amount);
        card.setDebt(nuevaDeuda);

        // 4. Si la deuda queda en cero, reiniciamos las cuotas a pagar
        if (nuevaDeuda.compareTo(BigDecimal.ZERO) == 0) {
            card.setNumberOfInstallments(0);
        }

        creditCardRepositoryPort.updateCreditCard(card);
        System.out.println("✅ Pago realizado correctamente. Deuda actual: $" + card.getDebt());
    }
}
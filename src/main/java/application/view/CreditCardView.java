package application.view;

import application.service.outputs.CreditCardService;
import application.domain.CreditCard;
import application.domain.PurchaseResult;
import application.util.FormValidationUtil;

import java.util.List;

public class CreditCardView {
    private final CreditCardService creditCardService;

    public CreditCardView(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }


    private void createCard() {
        String number = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        double quota = FormValidationUtil.validateDouble("Ingrese cupo disponible: ");
        double limit = FormValidationUtil.validateDouble("Ingrese límite de crédito: ");

        CreditCard card = new CreditCard(number, quota, limit);
        creditCardService.createCreditCard(card);

        System.out.println("✅ Tarjeta creada exitosamente.");
    }

    private void getCard() {
        String number = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        CreditCard card = creditCardService.getCard(number);

        if (card != null) {
            printCardDetails(card);
        } else {
            System.out.println("⚠️ Tarjeta no encontrada.");
        }
    }

    private void listCards() {
        List<CreditCard> cards = creditCardService.getAllCards();
        System.out.println("\nTarjetas disponibles:");
        for (CreditCard card : cards) {
            printCardDetails(card);
        }
    }

    private void purchase() {
        String number = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto de la compra: ");
        int installments = FormValidationUtil.validateInt("Ingrese número de cuotas: ");

        try {
            PurchaseResult result = creditCardService.purchaseCreditCard(number, amount, installments);

            System.out.println("\n💳 Compra realizada con tarjeta " + number);
            System.out.printf("Monto: $%.2f | Cuotas: %d%n", result.getAmount(), result.getInstallments());
            System.out.printf("Tasa aplicada: %.2f%%%n", result.getRate() * 100);
            System.out.printf("Cuota mensual: $%.2f%n", result.getMonthlyInstallment());
            System.out.printf("Total a pagar con intereses: $%.2f%n", result.getTotalWithInterest());
            System.out.printf("Nueva deuda acumulada (capital): $%.2f%n", result.getNewDebt());
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private void pay() {
        String number = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto del pago: ");

        try {
            creditCardService.pay(number, amount);
            System.out.println("✅ Pago realizado exitosamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private void printCardDetails(CreditCard card) {
        System.out.println("\n--------------------------------------");
        System.out.println("Número de tarjeta   : " + card.getAccountNumber());
        System.out.println("Cupo disponible     : " + card.getQuota());
        System.out.println("Límite de crédito   : " + card.getCreditLimit());
        System.out.println("Deuda actual        : " + card.getDebt());
        System.out.println("Cuotas últimas compra: " + card.getNumberOfInstallments());
        System.out.println("Estado              : " + card.getAccountState());
        System.out.println("--------------------------------------");
    }
}

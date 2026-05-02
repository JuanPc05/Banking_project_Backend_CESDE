package application.view;

import application.domain.CreditCard;
import application.domain.PurchaseResult;
import application.service.outputs.CreditCardService;
import application.util.FormValidationUtil;

public class CreditCardView {
    private final CreditCardService creditCardService;

    public CreditCardView(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    public void createCard() {
        String cardNumber = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        double quota = FormValidationUtil.validateDouble("Ingrese cupo de crédito: ");
        double creditLimit = FormValidationUtil.validateDouble("Ingrese límite máximo de crédito: ");

        CreditCard newCard = new CreditCard(cardNumber, quota, creditLimit);
        creditCardService.createCreditCard(newCard);

        System.out.println("\n✅ Tarjeta creada exitosamente: " + cardNumber);
    }

    public void getCard() {
        String cardNumber = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        CreditCard card = creditCardService.getCard(cardNumber);
        if (card != null) {
            System.out.println("Número: " + card.getAccountNumber() +
                    " | Cupo: " + card.getQuota() +
                    " | Límite: " + card.getCreditLimit() +
                    " | Deuda: " + card.getDebt());
        } else {
            System.out.println("⚠️ Tarjeta no encontrada.");
        }
    }

    public void getAllCards() {
        var cards = creditCardService.getAllCards();
        if (cards.isEmpty()) {
            System.out.println("⚠️ No hay tarjetas registradas.");
        } else {
            cards.forEach(card ->
                    System.out.println("Número: " + card.getAccountNumber() +
                            " | Cupo: " + card.getQuota() +
                            " | Límite: " + card.getCreditLimit() +
                            " | Deuda: " + card.getDebt()));
        }
    }

    public void purchase() {
        String cardNumber = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto de la compra: ");
        int installments = FormValidationUtil.validateInt("Ingrese número de cuotas: ");

        try {
            PurchaseResult result = creditCardService.purchaseCreditCard(cardNumber, amount, installments);
            System.out.println(result);
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    public void makePurchase() {
        String cardNumber = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto de la compra: ");
        int installments = FormValidationUtil.validateInt("Ingrese número de cuotas: ");
        try {
            PurchaseResult result = creditCardService.purchaseCreditCard(cardNumber, amount, installments);

            System.out.println("✅ Compra realizada con tarjeta " + cardNumber);
            System.out.println("Monto: $" + result.getAmount());
            System.out.println("Cuotas: " + result.getInstallments());
            System.out.println("Tasa aplicada: " + (result.getRate() * 100) + "%");


            System.out.printf("Cuota mensual: $%.0f%n", result.getMonthlyInstallment());
            System.out.printf("Total con intereses: $%.0f%n", result.getTotalWithInterest());

            System.out.println("Nueva deuda acumulada: $" + result.getNewDebt());


        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

}

package application.view;

import application.service.outputs.CreditCardService;
import application.util.FormValidationUtil;

public class CreditCardView {


    private final CreditCardService service;

    public CreditCardView(CreditCardService service) {
        this.service = service;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n--- MENÚ TARJETA DE CRÉDITO ---");
            System.out.println("1. Realizar compra");
            System.out.println("2. Pagar tarjeta");
            System.out.println("3. Calcular cuota mensual");
            System.out.println("0. Salir");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> purchase();
                case 2 -> pay();
                case 3 -> calculateInstallment();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (option != 0);
    }

    private void purchase() {
        String cardNumber = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto de la compra: ");
        int installments = FormValidationUtil.validateInt("Ingrese número de cuotas: ");
        service.purchase(cardNumber, amount, installments);
    }

    private void pay() {
        String cardNumber = FormValidationUtil.validateString("Ingrese número de tarjeta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto del pago: ");
        service.pay(cardNumber, amount);
    }

    private void calculateInstallment() {
        double amount = FormValidationUtil.validateDouble("Ingrese monto: ");
        int installments = FormValidationUtil.validateInt("Ingrese número de cuotas: ");
        double cuota = service.calculateMonthlyInstallment(amount, installments);
        System.out.println("La cuota mensual calculada es: $" + cuota);
    }
}

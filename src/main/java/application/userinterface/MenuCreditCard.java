package application.userinterface;

import application.view.CreditCardView;
import application.util.FormValidationUtil;

public class MenuCreditCard {
    private final CreditCardView creditCardView;

    public MenuCreditCard(CreditCardView creditCardView) {
        this.creditCardView = creditCardView;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n=== MENÚ TARJETAS DE CRÉDITO ===");
            System.out.println("1. Crear tarjeta");
            System.out.println("2. Consultar tarjeta");
            System.out.println("3. Ver todas las tarjetas");
            System.out.println("4. Realizar compra");
            System.out.println("0. Volver al menú principal");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> creditCardView.createCard();
                case 2 -> creditCardView.getCard();
                case 3 -> creditCardView.getAllCards();
                case 4 -> creditCardView.purchase();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }
}

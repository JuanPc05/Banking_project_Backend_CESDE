package application.userinterface;

import application.service.outputs.CheckingAccountService;
import application.service.outputs.SavingsAccountService;
import application.service.CreditCardServiceImpl;
import application.util.FormValidationUtil;
import application.view.CheckingAccountView;
import application.view.CreditCardView;
import application.view.SavingsAccountView;

public class MainMenuView {
    private final CheckingAccountView checkingAccountView;
    private final MenuSavingsAccount menuSavingsAccount;
    private final MenuCreditCard menuCreditCard;

    public MainMenuView(CheckingAccountService checkingService,
                        SavingsAccountView savingsAccountView,
                        CreditCardServiceImpl creditCardService) {
        this.checkingAccountView = new CheckingAccountView(checkingService);
        this.menuSavingsAccount = new MenuSavingsAccount(savingsAccountView);

        // Aquí creamos la vista de tarjetas y se la pasamos al menú
        CreditCardView creditCardView = new CreditCardView(creditCardService);
        this.menuCreditCard = new MenuCreditCard(creditCardView);
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL HAPIBANK ===");
            System.out.println("1. Cuenta Corriente");
            System.out.println("2. Cuenta de Ahorros");
            System.out.println("3. Tarjeta de Crédito");
            System.out.println("0. Salir");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> checkingAccountView.showMenu();
                case 2 -> menuSavingsAccount.showMenu();
                case 3 -> menuCreditCard.showMenu();
                case 0 -> System.out.println("Gracias por usar el sistema bancario CESDE. ¡Hasta pronto!");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }
}

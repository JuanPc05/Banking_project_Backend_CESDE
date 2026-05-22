package bank.userinterface;

import bank.domain.Client;
import bank.application.inputs.CheckingAccountService;
import bank.application.CreditCardServiceImpl;
import bank.infrastructure.util.FormValidationUtil;
import bank.infrastructure.in.view.adapter.CheckingAccountView;
import bank.infrastructure.in.view.adapter.CreditCardView;
import bank.infrastructure.in.view.adapter.SavingsAccountView;

public class MainMenuView {
    private final CheckingAccountView checkingAccountView;
    private final MenuSavingsAccount menuSavingsAccount;
    private final MenuCreditCard menuCreditCard;

    public MainMenuView(CheckingAccountService checkingService,
                        SavingsAccountView savingsAccountView,
                        CreditCardServiceImpl creditCardService) {
        this.checkingAccountView = new CheckingAccountView(checkingService);
        this.menuSavingsAccount = new MenuSavingsAccount(savingsAccountView);
        CreditCardView creditCardView = new CreditCardView(creditCardService);
        this.menuCreditCard = new MenuCreditCard(creditCardView);
    }

    public void showMenu(Client loggedInClient) {
        int option;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL HAPIBANK ===");
            System.out.println("Bienvenido/a, " + loggedInClient.getFullName());
            System.out.println("1. Cuenta Corriente");
            System.out.println("2. Cuenta de Ahorros");
            System.out.println("3. Tarjeta de Crédito");
            System.out.println("0. Cerrar sesión");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> checkingAccountView.showMenu();
                case 2 -> menuSavingsAccount.showMenu();
                case 3 -> menuCreditCard.showMenu();
                case 0 -> System.out.println("Cerrando sesión de " + loggedInClient.getUserName() + "... ¡Hasta pronto!");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }
}

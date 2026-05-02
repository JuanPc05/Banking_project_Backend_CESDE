package application.config;

import application.repository.CheckingAccountRepository;
import application.repository.SavingsAccountRepository;
import application.repository.CreditCardRepository;
import application.repository.ClientRepository;
import application.service.CheckingAccountServiceImpl;
import application.service.SavingsAccountServiceImpl;
import application.service.CreditCardServiceImpl;
import application.service.ClientServiceImpl;
import application.userinterface.MainMenuView;
import application.userinterface.HomeMenu;
import application.view.ClientView;
import application.view.CreditCardView;
import application.view.SavingsAccountView;

public class AppConfig {

    public static void main(String[] args) {
        // Arrancamos HomeMenu para exigir login
        HomeMenu menu = createHomeMenu();
        menu.start();
    }

    public static HomeMenu createHomeMenu() {
        // 1. Repositorios
        CheckingAccountRepository checkingRepo = new CheckingAccountRepository();
        SavingsAccountRepository savingsRepo = new SavingsAccountRepository();
        CreditCardRepository creditCardRepo = new CreditCardRepository();
        ClientRepository clientRepo = new ClientRepository();

        // 2. Servicios
        CheckingAccountServiceImpl checkingService = new CheckingAccountServiceImpl(checkingRepo);
        SavingsAccountServiceImpl savingsService = new SavingsAccountServiceImpl(savingsRepo);
        CreditCardServiceImpl creditCardService = new CreditCardServiceImpl(creditCardRepo);
        ClientServiceImpl clientService = new ClientServiceImpl(clientRepo);

        // 3. Vista de cliente (para login)
        ClientView clientView = new ClientView(clientService, clientService);
        SavingsAccountView savingsAccountView = new SavingsAccountView(savingsService);
        CreditCardView creditCardView = new CreditCardView(creditCardService);

        // 4. Menú principal (solo 3 parámetros)
        MainMenuView mainMenuView = new MainMenuView(
                checkingService,
                savingsAccountView,
                creditCardService
        );

        // 5. Retornamos el menú de entrada (Home)
        return new HomeMenu(clientView, mainMenuView);
    }
}

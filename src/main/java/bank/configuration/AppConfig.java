package bank.configuration;

import bank.infrastructure.out.adapter.CheckingAccountRepository;
import bank.infrastructure.out.adapter.SavingsAccountRepository;
import bank.infrastructure.out.adapter.CreditCardRepository;
import bank.infrastructure.out.adapter.ClientRepository;
import bank.application.service.CheckingAccountServiceImpl;
import bank.application.service.SavingsAccountServiceImpl;
import bank.application.service.CreditCardServiceImpl;
import bank.application.service.ClientServiceImpl;
import bank.userinterface.MainMenuView;
import bank.userinterface.HomeMenu;
import bank.infrastructure.in.view.adapter.ClientView;
import bank.infrastructure.in.view.adapter.CreditCardView;
import bank.infrastructure.in.view.adapter.SavingsAccountView;

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

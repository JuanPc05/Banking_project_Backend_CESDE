package application.config;

import application.repository.CheckingAccountRepository;
import application.repository.SavingsAccountRepository;
import application.repository.CreditCardRepository;
import application.repository.ClientRepository;
import application.service.CheckingAccountServiceImpl;
import application.service.SavingsAccountServiceImpl;
import application.service.CreditCardServiceImpl;
import application.service.ClientService;
import application.userinterface.MainMenuView;

public class AppConfig {

    public static void main(String[] args) {
        MainMenuView menu = createMenuApp();
        menu.showMenu();
    }

    public static MainMenuView createMenuApp() {

        CheckingAccountRepository checkingRepo = new CheckingAccountRepository();
        SavingsAccountRepository savingsRepo = new SavingsAccountRepository();
        CreditCardRepository creditCardRepo = new CreditCardRepository();
        ClientRepository clientRepo = new ClientRepository();


        CheckingAccountServiceImpl checkingService = new CheckingAccountServiceImpl(checkingRepo);
        SavingsAccountServiceImpl savingsService = new SavingsAccountServiceImpl(savingsRepo);
        CreditCardServiceImpl creditCardService = new CreditCardServiceImpl(creditCardRepo);
        ClientService clientService = new ClientService(clientRepo, creditCardRepo);


        return new MainMenuView(checkingService, savingsService, creditCardService, clientService);
    }
}

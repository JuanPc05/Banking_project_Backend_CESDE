package bank.configuration;

import bank.infrastructure.out.adapter.*;
import bank.infrastructure.out.db.DatabaseConnectionMySQL; // Importamos tu clase Singleton
import bank.application.CheckingAccountServiceImpl;
import bank.application.SavingsAccountServiceImpl;
import bank.application.CreditCardServiceImpl;
import bank.application.ClientServiceImpl;
import bank.userinterface.MainMenuView;
import bank.userinterface.HomeMenu;
import bank.infrastructure.in.view.adapter.ClientView;
import bank.infrastructure.in.view.adapter.CreditCardView;
import bank.infrastructure.in.view.adapter.SavingsAccountView;
import bank.infrastructure.out.mapper.ClientRowMapper;

import java.sql.Connection;

public class AppConfig {

    public static void main(String[] args) {
        // Al arrancar, el Singleton manejará cualquier error de conexión lanzando un RuntimeException
        HomeMenu menu = createHomeMenu();
        menu.start();
    }

    public static HomeMenu createHomeMenu() {

        // 0. OBTENEMOS LA CONEXIÓN A LA BASE DE DATOS
        // Llamamos a tu Singleton para obtener la única instancia de la conexión
        Connection connection = DatabaseConnectionMySQL.getInstance().getConnection();

        // Instanciamos el Mapper que convierte ResultSet a objetos Client
        ClientRowMapper clientRowMapper = new ClientRowMapper();

        // 1. Repositorios
        CheckingAccountRepository checkingRepo = new CheckingAccountRepository();
        SavingsAccountRepository savingsRepo = new SavingsAccountRepository();
        CreditCardRepository creditCardRepo = new CreditCardRepository();

        // Inyectamos la conexión y el mapper en el repositorio de base de datos
        ClientRepositoryDb clientRepo = new ClientRepositoryDb(connection, clientRowMapper);

        // 2. Servicios
        CheckingAccountServiceImpl checkingService = new CheckingAccountServiceImpl(checkingRepo);
        SavingsAccountServiceImpl savingsService = new SavingsAccountServiceImpl(savingsRepo);
        CreditCardServiceImpl creditCardService = new CreditCardServiceImpl(creditCardRepo);
        ClientServiceImpl clientService = new ClientServiceImpl(clientRepo);

        // 3. Vistas
        ClientView clientView = new ClientView(clientService, clientService);
        SavingsAccountView savingsAccountView = new SavingsAccountView(savingsService);
        CreditCardView creditCardView = new CreditCardView(creditCardService);

        // 4. Menús
        MainMenuView mainMenuView = new MainMenuView(
                checkingService,
                savingsAccountView,
                creditCardService
        );

        // 5. Retornamos el menú de entrada (Home)
        return new HomeMenu(clientView, mainMenuView);
    }
}
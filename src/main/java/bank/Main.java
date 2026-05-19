package bank;

import bank.configuration.AppConfig;
import bank.userinterface.HomeMenu;

public class Main {
    public static void main(String[] args) {
        // Obtenemos el menú ya ensamblado con todas sus dependencias
        HomeMenu menu = AppConfig.createHomeMenu();

        // Arrancamos el ciclo del menú principal
        menu.start();
    }
}

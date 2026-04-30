package application;

import application.config.AppConfig;
import application.userinterface.MainMenuView;

public class Main {
    public static void main(String[] args) {
        MainMenuView menu = AppConfig.createMenuApp();
        menu.showMenu();
    }
}

package bank.infrastructure.in.view.adapter;

import bank.application.inputs.IClientManagement;
import bank.domain.CheckingAccount;
import bank.application.inputs.CheckingAccountService;
import bank.infrastructure.util.FormValidationUtil;

import java.math.BigDecimal;

public class CheckingAccountView {
    private final CheckingAccountService checkingAccountService;
    private final IClientManagement clientManagement;
    private int currentClientId;

    public CheckingAccountView(CheckingAccountService checkingAccountService , IClientManagement clientManagement ) {
        this.checkingAccountService = checkingAccountService;
        this.clientManagement = clientManagement;


    }

    public void setLoggedInClientId(int clientId) {
        this.currentClientId = clientId;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n--- MENÚ CUENTA CORRIENTE ---");
            System.out.println("1. Crear cuenta");
            System.out.println("2. Consultar cuenta");
            System.out.println("3. Ver todas las cuentas");
            System.out.println("4. Depositar");
            System.out.println("5. Retirar");
            System.out.println("6. Transferir");
            System.out.println("7. Ver movimientos");
            System.out.println("0. Salir");

            option = FormValidationUtil.validateInt("Seleccione una opción: ");

            switch (option) {
                case 1 -> createAccount();
                case 2 -> getAccount();
                case 3 -> getAllAccounts();
                case 4 -> deposit();
                case 5 -> withdraw();
                case 6 -> transfer();
                case 7 -> showTransactions();
                case 0 -> System.out.println("👋 Saliendo...");
                default -> System.out.println("⚠️ Opción inválida.");
            }
        } while (option != 0);
    }

    private void createAccount() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        int clientId = FormValidationUtil.validateInt("Ingrese ID del cliente dueño: ");

        BigDecimal balance = FormValidationUtil.validateBigDecimal("Ingrese saldo inicial: ");
        BigDecimal overdraftLimit = FormValidationUtil.validateBigDecimal("Ingrese límite de sobregiro: ");
        double overdraftPercentage = FormValidationUtil.validateDouble("Ingrese porcentaje de sobregiro: ");

        // ✅ ORDEN CORREGIDO: Coincide perfectamente con los parámetros de tu entidad CheckingAccount
        CheckingAccount newAccount = new CheckingAccount(
                accountNumber,
                balance,
                "Cuenta Corriente",
                clientId,
                overdraftPercentage,
                overdraftLimit
        );

        checkingAccountService.createAccount(newAccount);
        System.out.println("\n✅ Cuenta creada exitosamente.");
    }

    private void getAccount() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        try {
            var account = checkingAccountService.getAccount(accountNumber);

            System.out.println(formatAccountForDisplay(account));
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }



    private void getAllAccounts() {
        var accounts = checkingAccountService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("⚠️ No hay cuentas registradas.");
        } else {
            // ✅ Usa el formateador local para cada cuenta
            accounts.forEach(account -> System.out.println(formatAccountForDisplay(account)));
        }
    }
    private void deposit() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a depositar: ");
        checkingAccountService.deposit(accountNumber, amount);
    }

    private void withdraw() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a retirar: ");
        checkingAccountService.withdraw(accountNumber, amount);
    }


    public void transfer() {
        System.out.println("\n--- TRANSFERENCIA CUENTA CORRIENTE ---");
        String fromAccount = FormValidationUtil.validateString("Ingrese número de cuenta corriente origen: ");
        String toAccount = FormValidationUtil.validateString("Ingrese número de cuenta destino: ");
        BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a transferir: ");

        checkingAccountService.transfer(fromAccount, toAccount, amount);
    }

    public void showTransactions() {
        try {
            // 1. Obtener la cuenta corriente del cliente logueado
            CheckingAccount account = checkingAccountService.getAccountByClientId(this.currentClientId);

            if (account == null) {
                System.out.println("⚠️ No tienes una cuenta corriente asociada.");
                return;
            }

            // 2. Obtener el nombre del titular logueado
            String clientName = clientManagement.getClient(account.getClientId()).getFullName();
            String accountNumber = account.getAccountNumber();
            java.util.List<bank.domain.Transaction> transactions = checkingAccountService.getTransactionsByAccount(accountNumber);

            System.out.println("\n📄 REPORTE DE MOVIMIENTOS");
            System.out.println("Titular: " + clientName);
            System.out.println("Cuenta: " + accountNumber);
            System.out.println("==================================================");

            for (bank.domain.Transaction t : transactions) {
                String fecha = (t.getTimestamp() != null)
                        ? t.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        : "Sin fecha";

                String detalleFinal = t.getDescription();

                // 3. Extraer el número de cuenta de la descripción si aplica
                String accountNum = extractAccountNumber(t.getDescription());
                if (accountNum != null) {
                    try {
                        // Usamos el nuevo método del servicio que busca en ambas tablas (Corrientes y Ahorros)
                        int targetClientId = checkingAccountService.getClientIdByAccountNumber(accountNum);

                        if (targetClientId != -1) {
                            var clientDest = clientManagement.getClient(targetClientId);
                            detalleFinal = t.getDescription() + " (Titular: " + clientDest.getFullName() + ")";
                        }
                    } catch (Exception e) {
                        // Si ocurre un error al buscar, se mantiene la descripción original sin romper el reporte
                    }
                }

                System.out.println("Fecha: " + fecha + " | Tipo: " + t.getTransactionType());
                System.out.println("Monto: $" + t.getAmount());
                System.out.println("Detalle: " + detalleFinal);
                System.out.println("--------------------------------------------------");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error al procesar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }




    private String extractAccountNumber(String description) {
        // Busca números en la cadena (asumiendo cuentas de 6 dígitos)
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d{6}");
        java.util.regex.Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }


    private String formatAccountForDisplay(CheckingAccount account) {
        return "--------------------------------------------------\n" +
                "Número de Cuenta: " + account.getAccountNumber() + "\n" +
                "Saldo: $" + account.getBalance() + "\n" +
                "ID Cliente: " + account.getClientId() + "\n" +
                "Límite Sobregiro: $" + account.getOverdraftLimit() + "\n" +
                "Porcentaje Sobregiro: " + account.getOverdraftPercentage() + "%\n" +
                "--------------------------------------------------";
    }



}
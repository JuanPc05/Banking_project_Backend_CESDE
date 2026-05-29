package bank.infrastructure.in.view.adapter;

import bank.application.inputs.IClientManagement;
import bank.domain.SavingsAccount;
import bank.application.inputs.SavingsAccountService;
import bank.infrastructure.util.FormValidationUtil;

import java.math.BigDecimal;

public class SavingsAccountView {
    private final SavingsAccountService savingsAccountService;
    private final IClientManagement clientManagement;



    public SavingsAccountView(SavingsAccountService savingsAccountService , IClientManagement  clientManagement ) {
        this.savingsAccountService = savingsAccountService;
        this.clientManagement = clientManagement;




    }

    public void createAccount() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        int clientId = FormValidationUtil.validateInt("Ingrese el ID del cliente: ");
        double initialBalance = FormValidationUtil.validateDouble("Ingrese saldo inicial: ");
        try {
            SavingsAccount newAccount = savingsAccountService.createAccount(accountNumber, BigDecimal.valueOf(initialBalance),clientId );
            System.out.println("✅ Cuenta creada correctamente con número: " + newAccount.getAccountNumber());
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    public void deposit() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a depositar: ");
        try {
            savingsAccountService.deposit(accountNumber, amount);
            System.out.println("✅ Depósito realizado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    public void withdraw() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a retirar: ");
        try {
            savingsAccountService.withdraw(accountNumber, amount);
            System.out.println("✅ Retiro realizado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    public void applyInterest() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        try {
            savingsAccountService.applyInterest(accountNumber);
            System.out.println("✅ Intereses aplicados correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    public void showBalance() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        try {
            SavingsAccount account = savingsAccountService.getAccount(accountNumber);
            System.out.println("💰 Saldo actual: $" + account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    public void showTransactions() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        try {
            // A. Primero obtenemos la cuenta para saber a qué cliente le pertenece (su clientId)
            SavingsAccount account = savingsAccountService.getAccount(accountNumber);
            int clientId = account.getClientId();

            // B. Buscamos el nombre del cliente de forma segura en su servicio usando tu interfaz real
            String clientName = "Desconocido";
            try {
                bank.domain.Client client = clientManagement.getClient(clientId);
                if (client != null) {
                    // 🔥 CORREGIDO: Ahora usa getFullName() que es el método real de tu clase Client
                    clientName = client.getFullName();
                }
            } catch (Exception e) {
                // Si el cliente no se encuentra o la interfaz no lo tiene implementado aún,
                // se queda como "Desconocido" sin romper la ejecución del menú.
            }

            // C. Obtenemos las transacciones directo desde MySQL
            java.util.List<bank.domain.Transaction> transactions = savingsAccountService.getTransactionsByAccount(accountNumber);

            if (transactions == null || transactions.isEmpty()) {
                System.out.println("📄 No hay movimientos registrados para la cuenta de " + clientName + ".");
            } else {
                // MUESTRA EL NOMBRE REAL DEL TITULAR EN EL ENCABEZADO:
                System.out.println("📄 Movimientos de la cuenta " + accountNumber + " - Cliente: " + clientName + ":");
                System.out.println("==================================================");
                for (bank.domain.Transaction t : transactions) {
                    System.out.println("Tipo: " + t.getTransactionType() +
                            " | Monto: $" + t.getAmount() +
                            " | Saldo Nuevo: $" + t.getBalanceAfterTransaction());
                    System.out.println("Detalle: " + t.getDescription());
                    System.out.println("--------------------------------------------------");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }


    public void transfer() {
        System.out.println("\n--- TRANSFERENCIA CUENTA DE AHORROS ---");

        // Captura y valida los datos utilizando tus utilidades del proyecto
        String fromAccount = FormValidationUtil.validateString("Ingrese número de cuenta origen: ");
        String toAccount = FormValidationUtil.validateString("Ingrese número de cuenta destino: ");
        java.math.BigDecimal amount = FormValidationUtil.validateBigDecimal("Ingrese monto a transferir: ");

        // Llama al método del servicio que creamos en el paso anterior
        savingsAccountService.transfer(fromAccount, toAccount, amount);
    }



}

package bank.infrastructure.in.view.adapter;

import bank.domain.SavingsAccount;
import bank.application.inputs.SavingsAccountService;
import bank.infrastructure.util.FormValidationUtil;

import java.math.BigDecimal;

public class SavingsAccountView {
    private final SavingsAccountService savingsAccountService;

    public SavingsAccountView(SavingsAccountService savingsAccountService) {
        this.savingsAccountService = savingsAccountService;
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
        double amount = FormValidationUtil.validateDouble("Ingrese monto a depositar: ");
        try {
            savingsAccountService.deposit(accountNumber, amount);
            System.out.println("✅ Depósito realizado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    public void withdraw() {
        String accountNumber = FormValidationUtil.validateString("Ingrese número de cuenta: ");
        double amount = FormValidationUtil.validateDouble("Ingrese monto a retirar: ");
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
            SavingsAccount account = savingsAccountService.getAccount(accountNumber);
            if (account.getTransactions().isEmpty()) {
                System.out.println("📄 No hay movimientos registrados.");
            } else {
                System.out.println("📄 Movimientos de la cuenta " + accountNumber + ":");
                account.getTransactions().forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }
}

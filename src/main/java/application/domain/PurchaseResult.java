package application.domain;

public class PurchaseResult {

    private final double amount;
    private final int installments;
    private final double rate;
    private final double monthlyInstallment;
    private final double totalWithInterest;
    private final double newDebt;

    public PurchaseResult(double amount, int installments, double rate,
                          double monthlyInstallment, double totalWithInterest, double newDebt) {
        this.amount = amount;
        this.installments = installments;
        this.rate = rate;
        this.monthlyInstallment = monthlyInstallment;
        this.totalWithInterest = totalWithInterest;
        this.newDebt = newDebt;
    }

    public double getAmount() { return amount; }
    public int getInstallments() { return installments; }
    public double getRate() { return rate; }
    public double getMonthlyInstallment() { return monthlyInstallment; }
    public double getTotalWithInterest() { return totalWithInterest; }
    public double getNewDebt() { return newDebt; }


    @Override
    public String toString() {
        return "✅ Compra realizada" +
                "\nMonto: $" + amount +
                "\nCuotas: " + installments +
                "\nTasa aplicada: " + (rate * 100) + "%" +
                "\nCuota mensual: $" + monthlyInstallment +
                "\nTotal con intereses: $" + totalWithInterest +
                "\nNueva deuda acumulada: $" + newDebt;
    }

}



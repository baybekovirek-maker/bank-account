import java.util.Objects;

public final class Balance {
    private final long amount;
    private final Currency currency;

    public Balance(long amount, Currency currency) {
        if (amount < 0) {
            throw new IllegalArgumentException("Баланс не может быть отрицательным");
        }

        this.amount = amount;
        this.currency = Objects.requireNonNull(currency, "Валюта не указана");
    }

    public long getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Balance add(long amount) {
        checkAmount(amount);


        return new Balance(Math.addExact(this.amount, amount), currency);
    }

    public Balance subtract(long amount) {
        checkAmount(amount);

        if (amount > this.amount) {
            throw new IllegalArgumentException("Недостаточно денег");
        }

        return new Balance(this.amount - amount, currency);
    }

    public Balance convertTo(Currency targetCurrency) {
        Objects.requireNonNull(targetCurrency, "Валюта не указана");

        if (currency == targetCurrency) {
            return new Balance(amount, currency);
        }


        long convertedAmount = Math.multiplyExact(
                amount, currency.getPriceInRublesKopecks()
        ) / targetCurrency.getPriceInRublesKopecks();

        return new Balance(convertedAmount, targetCurrency);
    }

    private void checkAmount(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной");
        }
    }

    @Override
    public String toString() {
        long whole = amount / 100;
        long remainder = amount % 100;

        return whole + "." + (remainder < 10 ? "0" : "")
                + remainder + " " + currency.getSymbol();
    }
}
import java.util.Objects;

public final class BankAccount {
    private Balance balance;
    private String ownerName;
    private long withdrawalLimit;

    public BankAccount(String ownerName, Balance balance, long withdrawalLimit) {
        setOwnerName(ownerName);
        this.balance = Objects.requireNonNull(balance, "Баланс не указан");
        setWithdrawalLimit(withdrawalLimit);
    }

    public Balance getBalance() {
        return balance;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public void setOwnerName(String ownerName) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не должно быть пустым");
        }

        this.ownerName = ownerName.trim();
    }

    public void setWithdrawalLimit(long withdrawalLimit) {
        if (withdrawalLimit < 0) {
            throw new IllegalArgumentException("Лимит не может быть отрицательным");
        }

        this.withdrawalLimit = withdrawalLimit;
    }

    public void deposit(long amount) {
        balance = balance.add(amount);
    }

    public void withdraw(long amount) {
        checkWithdrawal(amount);
        balance = balance.subtract(amount);
    }


    public void transferTo(BankAccount recipient, long amount) {
        Objects.requireNonNull(recipient, "Получатель не указан");

        if (recipient == this) {
            throw new IllegalArgumentException("Нельзя переводить самому себе");
        }

        checkWithdrawal(amount);
        Balance transferInRubles = new Balance(amount, balance.getCurrency())
                .convertTo(Currency.RUB);

        if (transferInRubles.getAmount() > 5_000_000) {
            throw new IllegalArgumentException(
                    "Ваша карта заблокирована по 115-ФЗ"
            );
        }

        Balance transfer = new Balance(amount, balance.getCurrency())
                .convertTo(recipient.balance.getCurrency());

        if (transfer.getAmount() == 0) {
            throw new IllegalArgumentException("Сумма перевода слишком маленькая");
        }


        Balance senderBalance = balance.subtract(amount);
        Balance recipientBalance = recipient.balance.add(transfer.getAmount());

        balance = senderBalance;
        recipient.balance = recipientBalance;
    }

    private void checkWithdrawal(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля");
        }

        if (amount > withdrawalLimit) {
            throw new IllegalArgumentException("Превышен лимит одной операции");
        }
    }
}
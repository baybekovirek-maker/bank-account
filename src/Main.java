public class Main {
    public static void main(String[] args) {
        Balance original = new Balance(10_000, Currency.RUB);
        Balance updated = original.add(5_000);

        System.out.println("Исходный баланс: " + original); // 100.00 ₽
        System.out.println("После прибавления: " + updated); // 150.00 ₽
        System.out.println("После вычитания: " + updated.subtract(2_000));

        BankAccount ivan = new BankAccount(
                "Иван",
                new Balance(960_000, Currency.RUB),
                500_000
        );

        BankAccount anna = new BankAccount(
                "Анна",
                new Balance(0, Currency.USD),
                10_000
        );

        ivan.deposit(10_000);
        ivan.withdraw(10_000);

        // 960 рублей превращаются в 10 долларов.
        ivan.transferTo(anna, 96_000);

        System.out.println("Иван: " + ivan.getBalance()); // 8640.00 ₽
        System.out.println("Анна: " + anna.getBalance()); // 10.00 $

        ivan.setOwnerName("Иван Петров");
        ivan.setWithdrawalLimit(600_000);

        try {
            ivan.deposit(-100);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            ivan.transferTo(anna, 700_000);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            anna.withdraw(2_000);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        BankAccount testAccount = new BankAccount(
                "Пётр",
                new Balance(10_000_000, Currency.RUB),
                10_000_000
        );

        try {
            testAccount.transferTo(anna, 5_000_100);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
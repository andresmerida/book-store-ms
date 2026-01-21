package dev.am.bookstore.web.practice.solid.ocp;

public class DiscountCalculatorBeforeOCP {
    public double calculateDiscount(String customerType, double amount) {
        if (customerType.equals("Regular")) {
            return amount * 0.1;
        } else if (customerType.equals("Premium")) {
            return amount * 0.2;
        }

        return 0;
    }

    static void main() {
        DiscountCalculatorBeforeOCP calculator = new DiscountCalculatorBeforeOCP();

        IO.println("Regular discount: " + calculator.calculateDiscount("Regular", 100));
        IO.println("Premium discount: " + calculator.calculateDiscount("Premium", 100));
    }
}

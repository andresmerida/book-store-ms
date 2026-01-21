package dev.am.bookstore.web.practice.solid.ocp;

public class DiscountCalculator {
    public double calculateDiscount(Discount discount, double amount) {
        return discount.calculateDiscount(amount);
    }
}

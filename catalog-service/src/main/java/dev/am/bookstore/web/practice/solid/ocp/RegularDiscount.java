package dev.am.bookstore.web.practice.solid.ocp;

public class RegularDiscount implements Discount {
    @Override
    public double calculateDiscount(double amount) {
        return amount * 0.1;
    }
}

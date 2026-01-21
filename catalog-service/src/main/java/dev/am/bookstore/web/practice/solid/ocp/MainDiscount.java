package dev.am.bookstore.web.practice.solid.ocp;

public class MainDiscount {
    static void main() {
        Discount regularDiscount = new RegularDiscount();
        Discount premiumDiscount = new PremiumDiscount();

        DiscountCalculator discountCalculator = new DiscountCalculator();

        IO.println("Regular discount: " + discountCalculator.calculateDiscount(regularDiscount, 100));
        IO.println("Premium discount: " + discountCalculator.calculateDiscount(premiumDiscount, 100));
    }
}

package dev.am.bookstore.web.practice.solid.lsp;

public class MainLSP {
    static void main() {
        Shape rectangle = new Rectangle(10, 20);
        Shape square = new Square(10);

        IO.println("Rectangle Area: " + rectangle.calculateArea());
        IO.println("Square Area: " + square.calculateArea());
    }
}

package dev.am.bookstore.web.practice.solid.isp;

public class SimplePrinter implements Printer {
    @Override
    public void printDocument() {
        IO.println("Printing document...");
    }
}

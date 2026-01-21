package dev.am.bookstore.web.practice.solid.isp.before;

public class SimplePrinter implements Printer {
    @Override
    public void printDocument() {
        IO.println("Printing document...");
    }

    @Override
    public void scanDocument() {
        // Not implemented
    }

    @Override
    public void faxDocument() {
        // Not implemented
    }

    static void main() {
        SimplePrinter printer = new SimplePrinter();
        printer.printDocument();
    }
}

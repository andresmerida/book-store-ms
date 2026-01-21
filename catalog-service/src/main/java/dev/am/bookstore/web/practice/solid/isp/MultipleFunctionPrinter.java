package dev.am.bookstore.web.practice.solid.isp;

import dev.am.bookstore.web.practice.solid.isp.before.Printer;

public class MultipleFunctionPrinter implements Printer, Scanner, Fax {
    @Override
    public void printDocument() {
        IO.println("Printing document...");
    }

    @Override
    public void scanDocument() {
        IO.println("Scanning document...");
    }

    @Override
    public void faxDocument() {
        IO.println("Faxing document...");
    }
}

package dev.am.bookstore.web.practice.solid.isp;

public class MainInterfaceSegregationPrinciple {
    static void main() {
        Printer simplePrinter = new SimplePrinter();
        simplePrinter.printDocument();

        MultipleFunctionPrinter multipleFunctionPrinter = new MultipleFunctionPrinter();
        multipleFunctionPrinter.printDocument();
        multipleFunctionPrinter.scanDocument();
        multipleFunctionPrinter.faxDocument();
    }
}

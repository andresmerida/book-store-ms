package dev.am.bookstore.web.practice.solid.isp;

public class ScanDocument implements Scanner {
    @Override
    public void scanDocument() {
        IO.println("Scanning document...");
    }
}

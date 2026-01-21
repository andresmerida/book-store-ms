package dev.am.bookstore.web.practice.solid.dip;

public class LightBulb implements Switchable {
    @Override
    public void turnOn() {
        IO.println("LightBulb is on");
    }

    @Override
    public void turnOff() {
        IO.println("LightBulb is off");
    }
}

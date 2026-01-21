package dev.am.bookstore.web.practice.solid.dip.before;

public class Switch {
    private LightBulb lightBulb;

    public Switch(LightBulb lightBulb) {
        this.lightBulb = lightBulb;
    }

    public void operate() {
        lightBulb.turnOn();
    }

    public void close() {
        lightBulb.turnOff();
    }

    static void main() {
        LightBulb lightBulb = new LightBulb();
        Switch switch1 = new Switch(lightBulb);
        switch1.operate();
    }
}

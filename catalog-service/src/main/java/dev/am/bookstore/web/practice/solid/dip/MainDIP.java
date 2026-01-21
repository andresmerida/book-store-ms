package dev.am.bookstore.web.practice.solid.dip;

public class MainDIP {
    static void main() {
        Switchable lightBulb = new LightBulb();
        Switch lightSwitch = new Switch(lightBulb);
        lightSwitch.operate();
    }
}

package dev.am.bookstore.web.practice.solid.srp;

public class MainSRP {
    static void main(String[] args) {
        User user = new User("Andres", "andresmerida1@gmail.com");
        UserFileManager userFileManager = new UserFileManager();
        userFileManager.saveUser(user);
        userFileManager.readUser();
    }
}

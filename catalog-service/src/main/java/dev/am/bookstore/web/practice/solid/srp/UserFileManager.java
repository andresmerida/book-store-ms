package dev.am.bookstore.web.practice.solid.srp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserFileManager {
    private static final Logger log = LoggerFactory.getLogger(UserFileManager.class);

    // Define paths as Path objects once to avoid duplication
    private static final Path APP_DATA_DIR = Paths.get(System.getProperty("user.home"), "appdata");
    private static final Path USER_DATA_FILE = APP_DATA_DIR.resolve("userdata.ser");

    /**
     * Saves user data using NIO and automatic directory creation.
     */
    public void saveUser(User user) {
        try {
            // Create directories if they don't exist (handles nested dirs better than mkdir)
            Files.createDirectories(APP_DATA_DIR);

            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(USER_DATA_FILE))) {
                oos.writeObject(user);
                log.info("User saved successfully to {}", USER_DATA_FILE);
            }
        } catch (IOException e) {
            log.error("Failed to save user data: {}", e.getMessage(), e);
        }
    }

    public void readUser() {
        if (!Files.exists(USER_DATA_FILE)) {
            log.warn("Load failed: File does not exist at {}", USER_DATA_FILE);
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(USER_DATA_FILE))) {
            User u = (User) ois.readObject();
            log.info("User loaded successfully from {}", USER_DATA_FILE);
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed to read user data: {}", e.getMessage(), e);
        }
    }
}

import persistence.PersistenceService;
import service.HotelRepository;
import ui.HotelMenu;

import java.util.Scanner;

/**
 * Application entry point.
 *
 * Fixes:
 *  - Large Class / Long Method: Main is now just a wiring/bootstrapping class.
 *    All logic lives in the appropriate service or UI layer.
 *  - Dead Code: the labelled break `x:` and convoluted continue logic are gone.
 */
public class Main {
    public static void main(String[] args) {
        PersistenceService persistence = new PersistenceService();
        HotelRepository    repository  = persistence.load();
        Scanner            sc          = new Scanner(System.in);

        HotelMenu menu = new HotelMenu(repository, sc);
        menu.run();

        // Persist state before exit — join() ensures save truly completes
        Thread saveThread = persistence.saveAsync(repository);
        try {
            saveThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Warning: save may be incomplete.");
        }
        System.out.println("\nData saved. Goodbye!");
    }
}

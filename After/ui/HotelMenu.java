package ui;

import model.RoomType;
import service.BookingService;
import service.HotelRepository;
import service.RoomInfoService;

import java.util.Scanner;

/**
 * Command-line menu for the hotel management system.
 *
 * Fixes:
 *  - Large Class: all UI / menu interaction is now here, not in Hotel or Main.
 *  - Switch Statement: room-type selection uses RoomType.fromMenuChoice() instead
 *                      of a manual switch.
 *  - Long Method: main loop delegates every action to a focused helper method.
 */
public class HotelMenu {

    private final BookingService  bookingService;
    private final RoomInfoService roomInfoService;
    private final Scanner         sc;

    public HotelMenu(HotelRepository repository, Scanner sc) {
        this.sc              = sc;
        this.bookingService  = new BookingService(repository, sc);
        this.roomInfoService = new RoomInfoService(repository);
    }

    public void run() {
        char wish;
        do {
            printMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1 -> handleRoomFeatures();
                case 2 -> handleAvailability();
                case 3 -> handleBooking();
                case 4 -> handleFoodOrder();
                case 5 -> handleCheckout();
                case 6 -> { return; }  // Exit
                default -> System.out.println("Invalid choice. Please try again.");
            }

            System.out.print("\nContinue? (y/n): ");
            wish = sc.next().charAt(0);
        } while (wish == 'y' || wish == 'Y');
    }

    // ── Menu handlers ──────────────────────────────────────────────────────────

    private void handleRoomFeatures() {
        RoomType type = selectRoomType();
        if (type != null) roomInfoService.showFeatures(type);
    }

    private void handleAvailability() {
        RoomType type = selectRoomType();
        if (type != null) roomInfoService.showAvailability(type);
    }

    private void handleBooking() {
        RoomType type = selectRoomType();
        if (type != null) bookingService.bookRoom(type);
    }

    private void handleFoodOrder() {
        System.out.print("Enter room number: ");
        int roomNumber = readInt();
        bookingService.orderFood(roomNumber);
    }

    private void handleCheckout() {
        System.out.print("Enter room number: ");
        int roomNumber = readInt();
        bookingService.checkout(roomNumber);
    }

    // ── Shared helpers ─────────────────────────────────────────────────────────

    /**
     * Prints the room-type sub-menu and returns the selected RoomType.
     * Fixes Switch Statement: replaces manual switch(ch2) with enum lookup.
     */
    private RoomType selectRoomType() {
        System.out.println("\nChoose room type:");
        RoomType[] types = RoomType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, types[i].getDisplayName());
        }
        System.out.print("Choice: ");
        int choice = readInt();
        try {
            return RoomType.fromMenuChoice(choice);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid room type choice.");
            return null;
        }
    }

    private void printMainMenu() {
        System.out.println("""
                \n============================================
                         Hotel Management System
                ============================================
                  1. Display room details
                  2. Display room availability
                  3. Book a room
                  4. Order food
                  5. Checkout
                  6. Exit
                ============================================
                Enter choice:\
                """);
    }

    private int readInt() {
        try {
            return sc.nextInt();
        } catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }
}

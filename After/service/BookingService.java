package service;

import exception.RoomNotAvailableException;
import model.FoodOrder;
import model.Guest;
import model.MenuItem;
import model.Room;
import model.RoomType;

import java.util.Scanner;

/**
 * Handles booking, checkout, and food-ordering workflows.
 *
 * Fixes:
 *  - Large Class: booking / deallocate / order logic extracted from Hotel.
 *  - Long Method: bookroom() was one massive method; split into focused helpers.
 *  - Duplicate Code: single generic bookRoom() replaces four near-identical switch cases.
 *  - Switch Statement: no more switch on room type — RoomType enum carries the metadata.
 */
public class BookingService {

    private final HotelRepository repository;
    private final Scanner         sc;

    public BookingService(HotelRepository repository, Scanner sc) {
        this.repository = repository;
        this.sc         = sc;
    }

    // ── Book a room ────────────────────────────────────────────────────────────

    public void bookRoom(RoomType type) {
        printAvailableRooms(type);

        System.out.print("\nEnter room number: ");
        int roomNumber;
        try {
            roomNumber = sc.nextInt();
        } catch (Exception e) {
            sc.nextLine(); // drain bad input
            System.out.println("Invalid input.");
            return;
        }

        try {
            repository.validateAvailable(roomNumber);
        } catch (RoomNotAvailableException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        Room room = repository.getRoomByNumber(roomNumber);

        Guest primary = readGuest("guest");
        if (type.isDouble()) {
            Guest secondary = readGuest("second guest");
            room.book(primary, secondary);
        } else {
            room.book(primary);
        }

        System.out.println("Room #" + roomNumber + " booked successfully.");
    }

    // ── Checkout ───────────────────────────────────────────────────────────────

    public void checkout(int roomNumber) {
        Room room;
        try {
            room = repository.getRoomByNumber(roomNumber);
        } catch (IllegalArgumentException e) {
            System.out.println("Room doesn't exist.");
            return;
        }

        if (!room.isOccupied()) {
            System.out.println("Room #" + roomNumber + " is already empty.");
            return;
        }

        System.out.println("Room occupied by: " + room.getPrimaryGuest().getName());
        System.out.print("Confirm checkout? (y/n): ");
        char confirm = sc.next().charAt(0);
        if (confirm == 'y' || confirm == 'Y') {
            room.calculateBill();
            room.vacate();
            System.out.println("Checkout successful. Room deallocated.");
        }
    }

    // ── Food orders ────────────────────────────────────────────────────────────

    public void orderFood(int roomNumber) {
        Room room;
        try {
            room = repository.getRoomByNumber(roomNumber);
        } catch (IllegalArgumentException e) {
            System.out.println("Room doesn't exist.");
            return;
        }

        if (!room.isOccupied()) {
            System.out.println("Room #" + roomNumber + " is not booked.");
            return;
        }

        printMenu();
        char wish;
        do {
            try {
                System.out.print("Select item: ");
                int itemCode = sc.nextInt();
                System.out.print("Quantity: ");
                int qty = sc.nextInt();

                MenuItem item  = MenuItem.fromCode(itemCode);
                FoodOrder order = new FoodOrder(item, qty);
                room.addFoodOrder(order);
                System.out.printf("Added: %s x%d = Rs. %.2f%n",
                        item.getDisplayName(), qty, order.getTotalPrice());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid item code.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input.");
                sc.nextLine();
                return;
            }

            System.out.print("Order more? (y/n): ");
            wish = sc.next().charAt(0);
        } while (wish == 'y' || wish == 'Y');
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    private void printAvailableRooms(RoomType type) {
        System.out.print("Available room numbers: ");
        for (Room room : repository.getRoomsOfType(type)) {
            if (!room.isOccupied()) {
                System.out.print(room.getRoomNumber() + " ");
            }
        }
        System.out.println();
    }

    private Guest readGuest(String label) {
        System.out.println("--- Details for " + label + " ---");
        sc.nextLine(); // drain newline left in buffer by previous nextInt/next
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Contact: ");
        String contact = sc.nextLine().trim();
        System.out.print("Gender: ");
        String gender = sc.nextLine().trim();
        return new Guest(name, contact, gender);
    }

    private void printMenu() {
        System.out.println("\n==========\n   Menu:  \n==========");
        for (MenuItem item : MenuItem.values()) {
            System.out.printf("%d. %-12s Rs.%.0f%n",
                    item.getCode(), item.getDisplayName(), item.getPricePerUnit());
        }
        System.out.println();
    }
}

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents one hotel room that can hold one or two guests.
 *
 * Fixes:
 *  - Data Class: Room now owns behavior (addFoodOrder, generateBill, getOccupantSummary)
 *                instead of being a passive data bag.
 *  - Inappropriate Intimacy: food list is accessed only through Room methods,
 *                            no external class reaches inside to mutate it.
 *  - Lazy Class / Holder: the old "holder" array-wrapper is gone; HotelRepository
 *                         manages Room arrays directly via RoomType metadata.
 *  - Duplicate Code: single-room / double-room distinction handled by Optional<Guest>
 *                    for the second guest slot.
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private final RoomType      roomType;
    private final int           roomNumber;      // guest-facing room number (e.g. 1, 11, 31, 41…)
    private       Guest         primaryGuest;
    private       Guest         secondaryGuest;  // null when single-occupancy or unbooked
    private final List<FoodOrder> foodOrders = new ArrayList<>();

    public Room(RoomType roomType, int roomNumber) {
        this.roomType   = roomType;
        this.roomNumber = roomNumber;
    }

    // ── Booking ────────────────────────────────────────────────────────────────

    public boolean isOccupied() {
        return primaryGuest != null;
    }

    /** Book a single-occupancy room. */
    public void book(Guest guest) {
        this.primaryGuest   = guest;
        this.secondaryGuest = null;
    }

    /** Book a double-occupancy room. */
    public void book(Guest primary, Guest secondary) {
        this.primaryGuest   = primary;
        this.secondaryGuest = secondary;
    }

    /** Vacate the room (checkout). */
    public void vacate() {
        this.primaryGuest   = null;
        this.secondaryGuest = null;
        this.foodOrders.clear();
    }

    // ── Food orders ────────────────────────────────────────────────────────────

    public void addFoodOrder(FoodOrder order) {
        foodOrders.add(order);
    }

    public List<FoodOrder> getFoodOrders() {
        return Collections.unmodifiableList(foodOrders);
    }

    // ── Billing ────────────────────────────────────────────────────────────────

    /**
     * Calculates and prints the full bill for this room.
     * Fixes: Long Method + Duplicate Code in original bill() switch-cases.
     */
    public double calculateBill() {
        double total = roomType.getChargePerDay();

        System.out.println("\n*******");
        System.out.println(" Bill: ");
        System.out.println("*******");
        System.out.printf("Room #%-4d (%s)%n", roomNumber, roomType.getDisplayName());
        System.out.printf("Room Charge         : Rs. %.2f%n", roomType.getChargePerDay());

        if (!foodOrders.isEmpty()) {
            System.out.println("\n==================");
            System.out.println("Food Charges:");
            System.out.println("==================");
            System.out.printf("%-12s%-10s%-10s%n", "Item", "Qty", "Price");
            System.out.println("---------------------------------");
            for (FoodOrder order : foodOrders) {
                System.out.println(order);
                total += order.getTotalPrice();
            }
        }

        System.out.println("---------------------------------");
        System.out.printf("TOTAL               : Rs. %.2f%n", total);
        return total;
    }

    // ── Display helpers ────────────────────────────────────────────────────────

    public String getOccupantSummary() {
        if (!isOccupied()) return "Room #" + roomNumber + " - AVAILABLE";
        StringBuilder sb = new StringBuilder();
        sb.append("Room #").append(roomNumber).append(" | ").append(primaryGuest);
        if (secondaryGuest != null) {
            sb.append("\n               | ").append(secondaryGuest);
        }
        return sb.toString();
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public RoomType getRoomType()        { return roomType;        }
    public int      getRoomNumber()      { return roomNumber;      }
    public Guest    getPrimaryGuest()    { return primaryGuest;    }
    public Guest    getSecondaryGuest()  { return secondaryGuest;  }
}

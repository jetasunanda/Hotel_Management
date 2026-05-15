package service;

import model.Room;
import model.RoomType;

/**
 * Handles read-only room information queries (features + availability).
 *
 * Fixes:
 *  - Large Class: display responsibility extracted from Hotel.
 *  - Switch Statement: features() and availability() were switch blocks on room type;
 *                      both now rely on RoomType enum metadata — no switch needed.
 *  - Duplicate Code: availability loop is one generic method, not four copies.
 */
public class RoomInfoService {

    private final HotelRepository repository;

    public RoomInfoService(HotelRepository repository) {
        this.repository = repository;
    }

    /** Print the features/pricing of a room type. */
    public void showFeatures(RoomType type) {
        System.out.println("\n--- " + type.getDisplayName() + " ---");
        System.out.println(type.getFeatures());
        System.out.printf("Charge per day: Rs. %.0f%n", type.getChargePerDay());
    }

    /** Print how many rooms of a given type are still free. */
    public void showAvailability(RoomType type) {
        int available = repository.countAvailable(type);
        System.out.printf("Available %s rooms: %d / %d%n",
                type.getDisplayName(), available, type.getCapacity());
    }

    /** Print the occupancy status for every room of a given type. */
    public void showAllRooms(RoomType type) {
        System.out.println("\n=== " + type.getDisplayName() + " ===");
        for (Room room : repository.getRoomsOfType(type)) {
            System.out.println(room.getOccupantSummary());
        }
    }
}

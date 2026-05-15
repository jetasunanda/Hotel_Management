package service;

import exception.RoomNotAvailableException;
import model.Room;
import model.RoomType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * Stores and manages the collection of all hotel rooms.
 *
 * Fixes:
 *  - Lazy Class: the old "holder" was just a dumb wrapper around four arrays;
 *                this class adds real behaviour (getRoom, countAvailable, etc.).
 *  - Large Class: room-storage responsibility is extracted out of Hotel,
 *                 which was doing far too much.
 *  - Duplicate Code: one generic array indexed by RoomType enum replaces
 *                    four separately-named arrays and the repetitive switch blocks.
 */
public class HotelRepository implements Serializable {
    private static final long serialVersionUID = 1L;

    /** One Room[] per RoomType, initialised from the enum's capacity. */
    private final Map<RoomType, Room[]> rooms = new EnumMap<>(RoomType.class);

    public HotelRepository() {
        for (RoomType type : RoomType.values()) {
            Room[] arr = new Room[type.getCapacity()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = new Room(type, type.toRoomNumber(i));
            }
            rooms.put(type, arr);
        }
    }

    // ── Core access ────────────────────────────────────────────────────────────

    /**
     * Returns the Room object for a given type and zero-based index.
     * Fixes: Inappropriate Intimacy — callers work with Room objects,
     *        never with raw array slots.
     */
    public Room getRoom(RoomType type, int index) {
        return rooms.get(type)[index];
    }

    /** Returns the Room for a guest-facing absolute room number (e.g. 15). */
    public Room getRoomByNumber(int roomNumber) {
        RoomType type  = RoomType.fromRoomNumber(roomNumber);
        int      index = type.toIndex(roomNumber);
        return getRoom(type, index);
    }

    /**
     * Returns a defensive copy of all rooms of a given type.
     * Fix: prevents callers from mutating the internal array.
     */
    public Room[] getRoomsOfType(RoomType type) {
        Room[] arr = rooms.get(type);
        return Arrays.copyOf(arr, arr.length);
    }

    // ── Availability helpers ───────────────────────────────────────────────────

    public int countAvailable(RoomType type) {
        int count = 0;
        for (Room room : rooms.get(type)) {
            if (!room.isOccupied()) count++;
        }
        return count;
    }

    /**
     * Books the room with the given room number if it is free.
     * Throws RoomNotAvailableException otherwise.
     * Fixes: the booking validation was buried inside a long switch in Hotel.bookroom().
     */
    public void validateAvailable(int roomNumber) throws RoomNotAvailableException {
        Room room = getRoomByNumber(roomNumber);
        if (room.isOccupied()) {
            throw new RoomNotAvailableException(roomNumber);
        }
    }
}

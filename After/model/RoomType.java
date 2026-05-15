package model;

/**
 * Enum representing the four room categories.
 * Fixes: Primitive Obsession (integer type codes 1-4 replaced by enum),
 *        Switch Statement (room metadata — capacity, price, features — is now
 *        owned by the enum, not scattered in switch blocks across Hotel).
 *
 * Room number ranges (as in original program):
 *   LUXURY_DOUBLE  : rooms  1–10
 *   DELUXE_DOUBLE  : rooms 11–30
 *   LUXURY_SINGLE  : rooms 31–40
 *   DELUXE_SINGLE  : rooms 41–60
 */
public enum RoomType {
    LUXURY_DOUBLE(
            "Luxury Double Room",
            10,
            1,        // display offset (first room number = 1)
            4000,
            true,
            "1 Double bed | AC | Free breakfast"
    ),
    DELUXE_DOUBLE(
            "Deluxe Double Room",
            20,
            11,
            3000,
            false,
            "1 Double bed | No AC | Free breakfast"
    ),
    LUXURY_SINGLE(
            "Luxury Single Room",
            10,
            31,
            2200,
            true,
            "1 Single bed | AC | Free breakfast"
    ),
    DELUXE_SINGLE(
            "Deluxe Single Room",
            20,
            41,
            1200,
            false,
            "1 Single bed | No AC | Free breakfast"
    );

    private final String displayName;
    private final int    capacity;       // total number of rooms of this type
    private final int    startRoomNo;    // first room number shown to guests
    private final double chargePerDay;
    private final boolean hasAC;
    private final String features;

    RoomType(String displayName, int capacity, int startRoomNo,
             double chargePerDay, boolean hasAC, String features) {
        this.displayName  = displayName;
        this.capacity     = capacity;
        this.startRoomNo  = startRoomNo;
        this.chargePerDay = chargePerDay;
        this.hasAC        = hasAC;
        this.features     = features;
    }

    public String  getDisplayName()  { return displayName;  }
    public int     getCapacity()     { return capacity;      }
    public int     getStartRoomNo()  { return startRoomNo;   }
    public double  getChargePerDay() { return chargePerDay;  }
    public boolean hasAC()           { return hasAC;         }
    public String  getFeatures()     { return features;      }

    /** True when this room type requires two guests. */
    public boolean isDouble() {
        return this == LUXURY_DOUBLE || this == DELUXE_DOUBLE;
    }

    /** Convert a guest-facing room number to an array index. */
    public int toIndex(int roomNumber) {
        return roomNumber - startRoomNo;
    }

    /** Convert an array index back to a guest-facing room number. */
    public int toRoomNumber(int index) {
        return index + startRoomNo;
    }

    /** Resolve the RoomType for an absolute room number (1-60). */
    public static RoomType fromRoomNumber(int roomNumber) {
        for (RoomType type : values()) {
            int end = type.startRoomNo + type.capacity - 1;
            if (roomNumber >= type.startRoomNo && roomNumber <= end) {
                return type;
            }
        }
        throw new IllegalArgumentException("Room number out of range: " + roomNumber);
    }

    /** Menu-choice ordinal (1-based) to enum. */
    public static RoomType fromMenuChoice(int choice) {
        RoomType[] values = values();
        if (choice < 1 || choice > values.length)
            throw new IllegalArgumentException("Invalid room type choice: " + choice);
        return values[choice - 1];
    }
}

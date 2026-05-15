package exception;

/**
 * Thrown when a guest attempts to book a room that is already occupied.
 */
public class RoomNotAvailableException extends Exception {
    private static final long serialVersionUID = 1L;

    public RoomNotAvailableException(int roomNumber) {
        super("Room #" + roomNumber + " is not available.");
    }
}

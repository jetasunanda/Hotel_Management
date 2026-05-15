package persistence;

import service.HotelRepository;

import java.io.*;

/**
 * Handles serialisation of the HotelRepository to / from disk.
 *
 * Fixes:
 *  - Large Class: I/O responsibility extracted from Hotel and the anonymous
 *                 "write" Runnable into a proper, named service.
 *  - The save still runs on a background thread so the UI is not blocked.
 */
public class PersistenceService {

    private static final String BACKUP_FILE = "backup";

    /** Load a persisted HotelRepository from disk, or return a fresh one. */
    public HotelRepository load() {
        File f = new File(BACKUP_FILE);
        if (!f.exists()) return new HotelRepository();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (HotelRepository) ois.readObject();
        } catch (Exception e) {
            System.out.println("Warning: could not load backup — starting fresh. (" + e.getMessage() + ")");
            return new HotelRepository();
        }
    }

    /**
     * Save the current HotelRepository to disk asynchronously.
     * Returns the Thread so the caller can join() it and wait for completion.
     */
    public Thread saveAsync(HotelRepository repository) {
        Thread t = new Thread(() -> {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BACKUP_FILE))) {
                oos.writeObject(repository);
            } catch (Exception e) {
                System.out.println("Error saving backup: " + e.getMessage());
            }
        });
        t.setDaemon(false); // ensure JVM waits for save to complete before exit
        t.start();
        return t;
    }
}

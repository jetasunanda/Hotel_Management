package model;

import java.io.Serializable;

/**
 * Represents a single hotel guest.
 * Fixes: Primitive Obsession — contact and gender are now encapsulated
 *        inside a dedicated Guest object with basic validation,
 *        rather than bare Strings scattered across room classes.
 */
public class Guest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String contact;
    private final String gender;

    public Guest(String name, String contact, String gender) {
        if (name    == null || name.isBlank())    throw new IllegalArgumentException("Name cannot be empty.");
        if (contact == null || contact.isBlank()) throw new IllegalArgumentException("Contact cannot be empty.");
        if (gender  == null || gender.isBlank())  throw new IllegalArgumentException("Gender cannot be empty.");
        this.name    = name;
        this.contact = contact;
        this.gender  = gender;
    }

    public String getName()    { return name;    }
    public String getContact() { return contact; }
    public String getGender()  { return gender;  }

    @Override
    public String toString() {
        return String.format("Name: %-20s | Contact: %-15s | Gender: %s", name, contact, gender);
    }
}

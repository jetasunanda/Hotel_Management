package model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum representing available food menu items.
 * Fixes: Primitive Obsession (integer item codes replaced with named enum),
 *        Switch Statement (price logic moved into enum itself).
 */
public enum MenuItem {
    SANDWICH(1, "Sandwich", 50),
    PASTA   (2, "Pasta",    60),
    NOODLES (3, "Noodles",  70),
    COKE    (4, "Coke",     30);

    private static final Map<Integer, MenuItem> BY_CODE = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(MenuItem::getCode, m -> m));

    private final int    code;
    private final String displayName;
    private final double pricePerUnit;

    MenuItem(int code, String displayName, double pricePerUnit) {
        this.code          = code;
        this.displayName   = displayName;
        this.pricePerUnit  = pricePerUnit;
    }

    public int    getCode()        { return code; }
    public String getDisplayName() { return displayName; }
    public double getPricePerUnit(){ return pricePerUnit; }

    /** Look up a MenuItem by its numeric menu code. O(1) via static map. */
    public static MenuItem fromCode(int code) {
        MenuItem item = BY_CODE.get(code);
        if (item == null) throw new IllegalArgumentException("Invalid menu item code: " + code);
        return item;
    }
}

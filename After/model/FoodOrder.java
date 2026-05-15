package model;

import java.io.Serializable;

/**
 * Represents a single food order line (item + quantity).
 * Fixes: Data Class smell — now owns its own price calculation logic
 * instead of delegating to a switch in Hotel.
 */
public class FoodOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    private final MenuItem menuItem;
    private final int quantity;
    private final double totalPrice;

    public FoodOrder(MenuItem menuItem, int quantity) {
        if (menuItem == null)
            throw new IllegalArgumentException("Menu item cannot be null.");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive.");
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.totalPrice = menuItem.getPricePerUnit() * quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return String.format("%-12s%-10d%-10.2f", menuItem.getDisplayName(), quantity, totalPrice);
    }
}

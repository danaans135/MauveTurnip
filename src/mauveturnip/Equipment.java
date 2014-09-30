package mauveturnip;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Equipment {
    private Item item;
//    private int amount;
    private IntegerProperty amount = new SimpleIntegerProperty();

    public IntegerProperty amountProperty() {
        return amount;
    }

    public Equipment(Item item) {
        this.item = item;
        this.amount.set(0);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }
}

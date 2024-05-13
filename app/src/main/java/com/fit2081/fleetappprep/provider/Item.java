package com.fit2081.fleetappprep.provider;

public class Item {
    public Item(String itemName, int quantity, int cost) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.cost = cost;
    }

    String itemName;
    int quantity;
    int cost;

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCost() {
        return cost;
    }
}

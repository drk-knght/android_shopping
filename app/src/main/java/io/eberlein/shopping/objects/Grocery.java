package io.eberlein.shopping.objects;

import io.realm.RealmObject;

public class Grocery extends RealmObject {
    private String name;
    private int count;
    private double price;

    public Grocery(){}

    public Grocery(String name, int count, double price){
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }
}

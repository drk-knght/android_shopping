package io.eberlein.shopping.events;

import io.eberlein.shopping.objects.Grocery;

public class EventGrocerySelected {
    private Grocery grocery;

    public EventGrocerySelected(Grocery grocery){
        this.grocery = grocery;
    }

    public Grocery getGrocery() {
        return grocery;
    }
}

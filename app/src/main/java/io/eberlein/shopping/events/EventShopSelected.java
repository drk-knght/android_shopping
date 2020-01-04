package io.eberlein.shopping.events;

import io.eberlein.shopping.objects.Shop;

public class EventShopSelected {
    private Shop shop;

    public EventShopSelected(Shop shop){
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }
}

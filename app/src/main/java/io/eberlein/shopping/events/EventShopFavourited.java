package io.eberlein.shopping.events;

import io.eberlein.shopping.objects.Shop;

public class EventShopFavourited {
    private Shop shop;

    public EventShopFavourited(Shop shop){
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }
}

package io.eberlein.shopping.objects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Shop extends RealmObject {
    @PrimaryKey
    private String name;
    private String description;
    private RealmList<Grocery> groceries;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public RealmList<Grocery> getGroceries() {
        return groceries;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

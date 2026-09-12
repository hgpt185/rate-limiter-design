package model;

import enums.Tier;

public class User {
    String id;
    Tier tier;

    public User(String id, Tier tier) {
        this.id = id;
        this.tier = tier;
    }

    public String getId() {
        return id;
    }

    public Tier getTier() {
        return tier;
    }
}
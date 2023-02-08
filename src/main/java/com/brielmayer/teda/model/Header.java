package com.brielmayer.teda.model;

public class Header {

    private final String name;
    private final boolean primaryKey;

    public Header(final String name, final boolean primaryKey) {
        this.name = name;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }
}

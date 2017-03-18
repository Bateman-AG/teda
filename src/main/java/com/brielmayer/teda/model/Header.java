package com.brielmayer.teda.model;

public class Header {

    private String name;
    private boolean primaryKey;

    public Header(String name, boolean primaryKey) {
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

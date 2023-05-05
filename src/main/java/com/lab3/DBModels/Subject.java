package com.lab3.DBModels;

import java.io.Serializable;

public class Subject implements Serializable {
    private int id = 0;
    private String name;

    public Subject(String name) {
        this.name = name;
    }

    public Subject(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Subject [id=" + id + ", name=" + name + "]";
    }
}


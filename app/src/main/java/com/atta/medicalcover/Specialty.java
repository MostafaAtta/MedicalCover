package com.atta.medicalcover;

import java.io.Serializable;

public class Specialty implements Serializable {


    private String name, image;

    private int id, order;

    private boolean top;

    public Specialty(String name, String image, int id, int order, boolean top) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.order = order;
        this.top = top;
    }

    public boolean isTop() {
        return top;
    }

    public Specialty() {
    }

    public Specialty(int id, String name, String image) {
        this.name = name;
        this.image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public int getOrder() {
        return order;
    }
}

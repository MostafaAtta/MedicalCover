package com.atta.medicalcover;

import java.io.Serializable;

public class Pharmacy implements Serializable {

    private String id, name, phone, image;

    public Pharmacy(){

    }

    public Pharmacy(String name, String phone, String image) {
        this.name = name;
        this.phone = phone;
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }
}

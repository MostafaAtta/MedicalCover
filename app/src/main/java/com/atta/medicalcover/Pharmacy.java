package com.atta.medicalcover;

import java.io.Serializable;

public class Pharmacy implements Serializable {

    String id, name, phone;

    public Pharmacy(){

    }

    public Pharmacy(String name, String phone) {
        this.name = name;
        this.phone = phone;
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
}

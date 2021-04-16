package com.atta.medicalcover;

import java.io.Serializable;

public class TestCenter implements Serializable {

    String id, name, phone, type;

    public TestCenter(){

    }

    public TestCenter(String name, String phone, String type) {
        this.name = name;
        this.phone = phone;
        this.type = type;
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

    public String getType() {
        return type;
    }
}

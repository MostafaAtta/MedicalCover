package com.atta.medicalcover;

import java.io.Serializable;

public class Allergy implements Serializable {

    private String id, name;

    public Allergy(){

    }

    public Allergy(String id, String name) {
        this.id = id;
        this.name = name;
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
}

package com.atta.medicalcover;

import java.io.Serializable;

public class FamilyHistory implements Serializable {

    private String id, member, description;

    public FamilyHistory(){

    }

    public FamilyHistory(String member, String description) {
        this.member = member;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getMember() {
        return member;
    }

    public String getDescription() {
        return description;
    }
}

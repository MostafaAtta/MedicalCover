package com.atta.medicalcover;

import java.io.Serializable;

public class LabTest implements Serializable {

    String test, result, normal;

    public LabTest() {

    }

    public LabTest(String test, String result, String normal) {
        this.test = test;
        this.result = result;
        this.normal = normal;
    }

    public String getTest() {
        return test;
    }

    public String getResult() {
        return result;
    }

    public String getNormal() {
        return normal;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }
}

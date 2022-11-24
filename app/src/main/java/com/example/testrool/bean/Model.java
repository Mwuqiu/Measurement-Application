package com.example.testrool.bean;

public class Model {
    private Integer modelId;
    private String name;
    private String date;
    private Double A,B;
    // y = Ax + b

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getA() {
        return A;
    }

    public void setA(Double a) {
        A = a;
    }

    public Double getB() {
        return B;
    }

    public void setB(Double b) {
        B = b;
    }
}

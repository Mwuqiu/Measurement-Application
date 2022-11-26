package com.example.testrool.bean;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.List;

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

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("modelName",name);
        jsonObject.put("date",date);
        jsonObject.put("A",A);
        jsonObject.put("B",B);
        return jsonObject;
    }
    public static Model fromJSONObject(JSONObject jsonObject) throws JSONException {
        //{"date":"2022-01-19 03:14:07","a":1,"modelName":"敌敌畏","b":2,"modelId":1,"userId":8}
        Model model = new Model();
        model.setModelId(jsonObject.getInt("modelId"));
        model.setName(jsonObject.getString("modelName"));
        model.setDate(jsonObject.getString("date"));
        model.setA(jsonObject.getDouble("a"));
        model.setB(jsonObject.getDouble("b"));
        return model;
    }


}

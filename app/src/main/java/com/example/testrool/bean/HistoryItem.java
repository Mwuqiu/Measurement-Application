package com.example.testrool.bean;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class HistoryItem {
    String itemName;
    String result;
    String date;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itemName",itemName);
        jsonObject.put("result",result);
        jsonObject.put("date",date);
        return jsonObject;
    }
    public static HistoryItem fromJSONObject(JSONObject x) {
        HistoryItem historyItem = new HistoryItem();
        historyItem.setItemName((String) x.get("itemName"));
        historyItem.setResult((String) x.get("result"));
        historyItem.setDate((String) x.get("date"));
        return historyItem;
    }

}

package com.ismartautocare;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SOAnswersMember {

    @SerializedName("response_status")
    @Expose
    private String response_status;
    @SerializedName("response_data")
    @Expose
    private JsonObject response_data = null;
    @SerializedName("result")
    @Expose
    private ArrayList<ItemMember> result = null;

    public ArrayList<ItemMember> getResult() {
        return result;
    }

    public void setResult(ArrayList<ItemMember> result) {
        this.result = result;
    }

    public String getResponse_status() {
        return response_status;
    }

    public void setResponse_status(String response_status) {
        this.response_status = response_status;
    }

    public JsonObject getItems() {
        return response_data;
    }

    public void setItems(JsonObject items) {
        this.response_data = items;
    }

}

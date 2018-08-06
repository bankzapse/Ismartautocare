package com.ismartautocare;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SOResponseOnServiceTest {

    @SerializedName("response_status")
    @Expose
    private String response_status;
    @SerializedName("response_data")
    @Expose
    private JsonObject response_data = null;
    @SerializedName("result")
    @Expose
    private ArrayList<ItemOfOnservice> result = null;

    public ArrayList<ItemOfOnservice> getResult() {
        return result;
    }

    public void setResult(ArrayList<ItemOfOnservice> result) {
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

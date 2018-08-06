package com.ismartautocare.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ismartautocare.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by programmer on 1/10/18.
 */

public class SOAnswersResponse {

    @SerializedName("response_status")
    @Expose
    private String response_status;
    @SerializedName("response_data")
    @Expose
    private ArrayList<Item> response_data = null;
    @SerializedName("result")
    @Expose
    private ArrayList<Item> result = null;

    public ArrayList<Item> getResult() {
        return result;
    }

    public void setResult(ArrayList<Item> result) {
        this.result = result;
    }

    public String getResponse_status() {
        return response_status;
    }

    public void setResponse_status(String response_status) {
        this.response_status = response_status;
    }

    public List<Item> getItems() {
        return response_data;
    }

    public void setItems(ArrayList<Item> items) {
        this.response_data = items;
    }

}

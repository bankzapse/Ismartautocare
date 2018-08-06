package com.ismartautocare;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SOMemberType {

    @SerializedName("response_status")
    @Expose
    private String response_status;
    @SerializedName("response_data")
    @Expose
    private ArrayList<ItemMemberType> response_data = null;
    @SerializedName("result")
    @Expose
    private ArrayList<ItemMemberType> result = null;

    public ArrayList<ItemMemberType> getResult() {
        return result;
    }

    public void setResult(ArrayList<ItemMemberType> result) {
        this.result = result;
    }

    public String getResponse_status() {
        return response_status;
    }

    public void setResponse_status(String response_status) {
        this.response_status = response_status;
    }

    public List<ItemMemberType> getItems() {
        return response_data;
    }

    public void setItems(ArrayList<ItemMemberType> items) {
        this.response_data = items;
    }

}

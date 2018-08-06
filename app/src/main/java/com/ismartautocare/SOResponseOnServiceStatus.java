package com.ismartautocare;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by programmer on 3/5/18.
 */

public class SOResponseOnServiceStatus {

    @SerializedName("response_status")
    @Expose
    private String response_status;
    @SerializedName("response_data")
    @Expose
    private String response_data = null;
    @SerializedName("result")
    @Expose
    private String result = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponse_status() {
        return response_status;
    }

    public void setResponse_status(String response_status) {
        this.response_status = response_status;
    }

    public String getItems() {
        return response_data;
    }

    public void setItems(String items) {
        this.response_data = items;
    }

}

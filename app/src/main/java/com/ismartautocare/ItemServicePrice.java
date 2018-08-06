package com.ismartautocare;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemServicePrice implements Serializable {

    @SerializedName("period")
    @Expose
    JsonObject period;
    @SerializedName("price")
    @Expose
    JsonObject price;
    @SerializedName("desc")
    @Expose
    String desc;
    @SerializedName("code")
    @Expose
    String code;

    public JsonObject getPeriod() {
        return period;
    }

    public void setPeriod(JsonObject period) {
        this.period = period;
    }

    public JsonObject getPrice() {
        return price;
    }

    public void setPrice(JsonObject price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



}

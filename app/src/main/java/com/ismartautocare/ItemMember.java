package com.ismartautocare;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemMember implements Serializable {

    @SerializedName("key")
    @Expose
    String key;
    @SerializedName("access_token")
    @Expose
    String access_token;
    @SerializedName("refresh_token")
    @Expose
    String refresh_token;
    @SerializedName("image")
    @Expose
    String image;
    @SerializedName("brand")
    @Expose
    String brand;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("models")
    @Expose
    JsonArray models;
    @SerializedName("size")
    @Expose
    String size;
    @SerializedName("contact")
    @Expose
    String contact;
    @SerializedName("desc")
    @Expose
    String desc;
    @SerializedName("_id")
    @Expose
    String _id;
    @SerializedName("fee")
    @Expose
    JsonObject fee;
    @SerializedName("price")
    @Expose
    JsonObject price_list;
    @SerializedName("plates")
    @Expose
    JsonObject plates;
    @SerializedName("expired_date")
    @Expose
    String expired_date;
    @SerializedName("set_price")
    @Expose
    String price;
    @SerializedName("set_period")
    @Expose
    String period;
    @SerializedName("period")
    @Expose
    JsonObject period_list;
    @SerializedName("code")
    @Expose
    String code;
    @SerializedName("served")
    @Expose
    JsonObject served;
    @SerializedName("plate")
    @Expose
    String plate;
    @SerializedName("province")
    @Expose
    String province;
    @SerializedName("car_models")
    @Expose
    JsonObject car_models;
    @SerializedName("driver")
    @Expose
    String driver;
    @SerializedName("address")
    @Expose
    String address;
    @SerializedName("created")
    @Expose
    String created;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public JsonObject getCar_models() {
        return car_models;
    }

    public void setCar_models(JsonObject car_models) {
        this.car_models = car_models;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public JsonObject getServed() {
        return served;
    }

    public void setServed(JsonObject served) {
        this.served = served;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public JsonObject getPeriod_list() {
        return period_list;
    }

    public void setPeriod_list(JsonObject period_list) {
        this.period_list = period_list;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public JsonObject getPrice_list() {
        return price_list;
    }

    public void setPrice_list(JsonObject price_list) {
        this.price_list = price_list;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
    }

    public JsonObject getPlates() {
        return plates;
    }

    public void setPlates(JsonObject plates) {
        this.plates = plates;
    }

    public JsonObject getFee() {
        return fee;
    }

    public void setFee(JsonObject fee) {
        this.fee = fee;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public JsonArray getModels() {
        return models;
    }

    public void setModels(JsonArray models) {
        this.models = models;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

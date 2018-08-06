package com.ismartautocare;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by programmer on 2/22/18.
 */

public class ItemOfAll implements Serializable {

    @SerializedName("_id")
    @Expose
    String id;
    @SerializedName("plate")
    @Expose
    JsonObject plate;
    @SerializedName("member")
    @Expose
    JsonElement member;
    @SerializedName("rno")
    @Expose
    String rno;
    @SerializedName("price")
    @Expose
    String price;
    @SerializedName("discount")
    @Expose
    String discount;
    @SerializedName("discount_detail")
    @Expose
    String discount_detail;
    @SerializedName("total")
    @Expose
    String total;
    @SerializedName("served")
    @Expose
    JsonObject served;
    @SerializedName("services")
    @Expose
    ArrayList<ItemOfAll> services;
    @SerializedName("payment")
    @Expose
    JsonObject payment;
    @SerializedName("esimate_time")
    @Expose
    String esimate_time;
    @SerializedName("checked")
    @Expose
    JsonObject checked;
//    @SerializedName("released")
//    @Expose
//    String released;
    @SerializedName("companyID")
    @Expose
    String companyID;
    @SerializedName("branchID")
    @Expose
    String branchID;
    @SerializedName("member_type")
    @Expose
    JsonObject member_type;
    @SerializedName("size")
    @Expose
    String size;
    @SerializedName("desc")
    @Expose
    String desc;
    @SerializedName("released")
    @Expose
    JsonObject released;
    @SerializedName("time")
    @Expose
    String time;
    @SerializedName("code")
    @Expose
    String code;
    @SerializedName("created")
    @Expose
    String created;
    @SerializedName("address")
    @Expose
    String address;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("plates")
    @Expose
    JsonObject plates;
    @SerializedName("issuedfrom")
    @Expose
    String issuedfrom;
    @SerializedName("issuedby")
    @Expose
    String issuedby;
    @SerializedName("model")
    @Expose
    JsonObject model;

    public JsonObject getModel() {
        return model;
    }

    public void setModel(JsonObject model) {
        this.model = model;
    }

    public String getIssuedfrom() {
        return issuedfrom;
    }

    public void setIssuedfrom(String issuedfrom) {
        this.issuedfrom = issuedfrom;
    }

    public String getIssuedby() {
        return issuedby;
    }

    public void setIssuedby(String issuedby) {
        this.issuedby = issuedby;
    }


    public JsonObject getPlates() {
        return plates;
    }

    public void setPlates(JsonObject plates) {
        this.plates = plates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public JsonObject getReleased() {
        return released;
    }

    public void setReleased(JsonObject released) {
        this.released = released;
    }
    public String getRno() {
        return rno;
    }

    public void setRno(String rno) {
        this.rno = rno;
    }

    public JsonObject getChecked() {
        return checked;
    }

    public void setChecked(JsonObject checked) {
        this.checked = checked;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public JsonObject getMember_type() {
        return member_type;
    }

    public void setMember_type(JsonObject member_type) {
        this.member_type = member_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonObject getPlate() {
        return plate;
    }

    public void setPlate(JsonObject plate) {
        this.plate = plate;
    }

    public JsonElement getMember() {
        return member;
    }

    public void setMember(JsonElement member) {
        this.member = member;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount_detail() {
        return discount_detail;
    }

    public void setDiscount_detail(String discount_detail) {
        this.discount_detail = discount_detail;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public JsonObject getServed() {
        return served;
    }

    public void setServed(JsonObject served) {
        this.served = served;
    }

    public ArrayList<ItemOfAll> getServices() {
        return services;
    }

    public void setServices(ArrayList<ItemOfAll> services) {
        this.services = services;
    }

    public JsonObject getPayment() {
        return payment;
    }

    public void setPayment(JsonObject payment) {
        this.payment = payment;
    }

    public String getEsimate_time() {
        return esimate_time;
    }

    public void setEsimate_time(String esimate_time) {
        this.esimate_time = esimate_time;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

}

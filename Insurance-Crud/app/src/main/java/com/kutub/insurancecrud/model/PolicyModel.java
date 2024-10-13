package com.kutub.insurancecrud.model;

import java.util.Date;

public class PolicyModel {

    private int id;
    private String date;
    private String bankName;
    private String policyHolder;
    private String address;
    private String stockItem;
    private int sumInsurd;


    // Default constructor
    public PolicyModel() {
    }

    public PolicyModel(int id) {
        this.id = id;
    }

    public PolicyModel(int id, String date, String bankName, String policyHolder, String address, String stockItem, int sumInsurd) {
        this.id = id;
        this.date = date;
        this.bankName = bankName;
        this.policyHolder = policyHolder;
        this.address = address;
        this.stockItem = stockItem;
        this.sumInsurd = sumInsurd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(String policyHolder) {
        this.policyHolder = policyHolder;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStockItem() {
        return stockItem;
    }

    public void setStockItem(String stockItem) {
        this.stockItem = stockItem;
    }

    public int getSumInsurd() {
        return sumInsurd;
    }

    public void setSumInsurd(int sumInsurd) {
        this.sumInsurd = sumInsurd;
    }
}

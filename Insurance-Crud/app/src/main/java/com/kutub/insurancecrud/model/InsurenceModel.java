package com.kutub.insurancecrud.model;

public class InsurenceModel {

    private int id;
    private String bankName;
    private String policyHolder;
    private String address;
    private String stockItem;
    private String sumInsurd;
    private String key;

    // Default constructor
    public InsurenceModel() {
    }

    // Constructor with all fields
    public InsurenceModel(int id, String bankName, String policyHolder, String address, String stockItem, String sumInsurd, String key) {
        this.id = id;
        this.bankName = bankName;
        this.policyHolder = policyHolder;
        this.address = address;
        this.stockItem = stockItem;
        this.sumInsurd = sumInsurd;
        this.key = key;
    }

    // Constructor with only id
    public InsurenceModel(int id) {
        this.id = id;
    }

    // Getter and setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and setter for bankName
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    // Getter and setter for policyHolder
    public String getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(String policyHolder) {
        this.policyHolder = policyHolder;
    }

    // Getter and setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and setter for stockItem
    public String getStockItem() {
        return stockItem;
    }

    public void setStockItem(String stockItem) {
        this.stockItem = stockItem;
    }

    // Getter and setter for sumInsurd
    public String getSumInsurd() {
        return sumInsurd;
    }

    public void setSumInsurd(String sumInsurd) {
        this.sumInsurd = sumInsurd;
    }

    // Getter and setter for key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

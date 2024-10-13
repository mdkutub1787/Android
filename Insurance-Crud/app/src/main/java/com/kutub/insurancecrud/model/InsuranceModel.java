package com.kutub.insurancecrud.model;

public class InsuranceModel {

    private int id;
    private String bankName;
    private String policyHolder;
    private String address;
    private String stockItem;
    private int sumInsurd;
    private String key;

    // Default constructor
    public InsuranceModel() {
    }

    // Constructor with all fields
    public InsuranceModel(int id, String bankName, String policyHolder, String address, String stockItem, int sumInsurd, String key) { // Updated type of sumInsurd
        this.id = id;
        this.bankName = bankName;
        this.policyHolder = policyHolder;
        this.address = address;
        this.stockItem = stockItem;
        this.sumInsurd = sumInsurd; // Updated
        this.key = key;
    }

    // Constructor with only id
    public InsuranceModel(int id) {
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
    public int getSumInsurd() {
        return sumInsurd;
    }

    public void setSumInsurd(int sumInsurd) {
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

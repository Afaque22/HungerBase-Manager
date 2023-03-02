package com.example.hungerbasemanager.Models;

public class OrdersModel {
    String itemName, orderPrice, orderQuanity, userName, orderAddress;

    public OrdersModel() {
    }

    public OrdersModel(String itemName, String orderPrice, String orderQuanity, String userName, String orderAddress) {
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.orderQuanity = orderQuanity;
        this.userName = userName;
        this.orderAddress = orderAddress;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderQuanity() {
        return orderQuanity;
    }

    public void setOrderQuanity(String orderQuanity) {
        this.orderQuanity = orderQuanity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }
}

package com.example.hungerbasemanager.Models;

public class MenuModel {
    String itemName;
    String desc;
    String price;

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    String itemImg;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;

    public MenuModel(String itemName, String desc, String price, String userId,String itemImg) {
        this.itemName = itemName;
        this.price = price;
        this.desc = desc;
        this.userId = userId;
        this.itemImg = itemImg;
    }

    public MenuModel() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

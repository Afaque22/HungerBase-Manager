package com.example.hungerbasemanager.Models;

public class DetialsModel {
    String ownerName;
    String resName;
    String phoneNo;
    String province;
    String city;
    String address;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String userID;

    public String getResImage() {
        return resImage;
    }

    public void setResImage(String resImage) {
        this.resImage = resImage;
    }

    String resImage;

    public DetialsModel(String ownerName, String resName, String phoneNo, String province, String city, String address, String userID) {
        this.ownerName = ownerName;
        this.resName = resName;
        this.phoneNo = phoneNo;
        this.province = province;
        this.city = city;
        this.address = address;
        this.userID = userID;
    }

    public DetialsModel() {
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

package com.android.watersprinkle;

public class Customer  {

    public String custName;
    public String image;
    public  String custAddress;
    public String uid;
    public String date;
    public String bottleType;
    public int bottlePrice;
    public int quantity;
    public int total;
    public String timestamp;


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public  Customer(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBottleType() {
        return bottleType;
    }

    public void setBottleType(String bottleType) {
        this.bottleType = bottleType;
    }

    public int getBottlePrice() {
        return bottlePrice;
    }

    public void setBottlePrice(int bottlePrice) {
        this.bottlePrice = bottlePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public  Customer(String date, String bottleType, int bottlePrice, int quantity, int total){
        this.date=date;
        this.bottleType=bottleType;
        this.bottlePrice=bottlePrice;
        this.quantity=quantity;
        this.total=total;
    }

}
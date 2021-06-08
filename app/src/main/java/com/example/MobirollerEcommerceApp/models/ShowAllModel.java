package com.example.MobirollerEcommerceApp.models;

import java.io.Serializable;

public class ShowAllModel implements Serializable {

    String description;
    String name;
    String company_name;
    String price;
    String img_url;
    String rating;
    String type;
    String item_id;
    String stock;
    int type_id;


    public ShowAllModel() {
    }

    public ShowAllModel(String description, String name, String price, String img_url, String rating, String type,String item_id,String company_name,String stock,int type_id) {
        this.description = description;
        this.name = name;
        this.price = price;
        this.img_url = img_url;
        this.rating = rating;
        this.type = type;
        this.item_id=item_id;
        this.company_name=company_name;
        this.stock=stock;
        this.type_id=type_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
